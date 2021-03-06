package org.ufa42conf

import twitter4j._
import twitter4j.conf.ConfigurationBuilder
import scala.collection.JavaConversions._
import akka.actor.Scheduler
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class TwitterBot(key: String, secret: String, scheduler: Scheduler) {

  private[this] var subscriptions = List[String]()
  private[this] val client = {
    val builder = new ConfigurationBuilder()
    builder.setApplicationOnlyAuthEnabled(true)
    val twitter = new TwitterFactory(builder.build()).getInstance()
    twitter.setOAuthConsumer(key, secret)
    twitter.getOAuth2Token()
    twitter
  }

  var tweets = List[Tweet]()
  var allTweets = List[Tweet]()

  def fetchUsers(userIds: Set[Long]): List[User] =
    client.lookupUsers(userIds.toArray: _*).map(User(_)).toList
//    userIds.map(id => User(client.showUser(id))).toList

  def addSubscription(queryStr: String): Unit = {
    subscriptions ::= queryStr
    fetchFeed(queryStr)
  }

  private def fetchFeed(queryStr: String): Unit = {
    println(s"updating $queryStr feed")
    var nextRunAfter = 30
    val query = new Query(queryStr)

    if (!tweets.isEmpty) {
      var mx = tweets.map(_.id).max
      query.setSinceId(mx)
      println("Max ID: " + mx)
    }

    query.setCount(100)

    try {
      val result = client.search(query)
      val limit = result.getRateLimitStatus

     


     
      val newTweets = result.getTweets.filterNot(_.getText.startsWith("RT")).map(Tweet(_)).toList.reverse

      for (tweet <- newTweets) println(s"${tweet.id}: ${tweet.text}")
      if (!newTweets.isEmpty)
        println("=======================")

      allTweets = allTweets ::: newTweets
      tweets = tweets ::: newTweets.filterNot(_.text.startsWith("@"))

      nextRunAfter = 2 * (if (limit.getRemaining > 0) subscriptions.length * limit.getSecondsUntilReset / limit.getRemaining else 5)
    } catch {
      case e: TwitterException =>
        e.printStackTrace()
        client.invalidateOAuth2Token()
        client.getOAuth2Token()
      case _: Throwable =>
        // ignore
    } finally {
      scheduler.scheduleOnce(nextRunAfter.seconds, new Runnable {
        override def run(): Unit = fetchFeed(queryStr)
      })
    }
  }
}
