package sample

import twitter4j._
import twitter4j.conf.ConfigurationBuilder
import scala.collection.JavaConversions._
import akka.actor.Scheduler
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class TwitterBot(key: String, secret: String, scheduler: Scheduler) {

  private[this] var subscriptions = List[String]()
  private[this] var lastId = 0L
  private[this] val client = {
    val builder = new ConfigurationBuilder()
    builder.setApplicationOnlyAuthEnabled(true)
    val twitter = new TwitterFactory(builder.build()).getInstance()
    twitter.setOAuthConsumer(key, secret)
    twitter.getOAuth2Token()
    twitter
  }

  var tweets = List[Tweet]()

  def fetchUsers(userIds: Set[Long]): List[User] =
    userIds.map(id => User(client.showUser(id))).toList

  def addSubscription(queryStr: String): Unit = {
    subscriptions ::= queryStr
    fetchFeed(queryStr)
  }

  private def fetchFeed(queryStr: String): Unit = {
    println(s"updating $queryStr feed")
    var nextRunAfter = 30
    val query = new Query(queryStr)
    query.setSinceId(lastId)

    try {
//      client.getRateLimitStatus("search").get("/search/tweets")
      val result = client.search(query)
      lastId = result.getMaxId
      val limit = result.getRateLimitStatus

      tweets ++= result.getTweets.filterNot(t => t.getText.startsWith("RT") || t.getText.startsWith("@")).map { t =>
        val loc = t.getGeoLocation
        Tweet(
          t.getId,
          t.getCreatedAt.getTime,
          Option(t.getPlace).map(place => Place(place.getName, loc.getLatitude, loc.getLongitude, place.getStreetAddress, "")),
          t.getText,
          User(t.getUser)
        )
      }

      nextRunAfter = 2 * (if (limit.getRemaining > 0) subscriptions.length * limit.getSecondsUntilReset / limit.getRemaining else 5)
    } catch {
      case e: TwitterException =>
        e.printStackTrace()
        client.invalidateOAuth2Token()
        client.getOAuth2Token()
    } finally {
      scheduler.scheduleOnce(nextRunAfter.seconds, new Runnable {
        override def run(): Unit = fetchFeed(queryStr)
      })
    }
  }
}