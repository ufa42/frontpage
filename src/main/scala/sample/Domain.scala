package sample

import spray.json.DefaultJsonProtocol


case class User(id: Long, name: String, fullname: String, avatarUrl: String)

object User extends DefaultJsonProtocol {
  implicit val _ = jsonFormat4(User.apply)

  def apply(user: twitter4j.User): User =
    User(user.getId, user.getScreenName, user.getName, user.getBiggerProfileImageURLHttps)
}

case class Talk(title: String, description: String, speaker: User)

object Talk extends DefaultJsonProtocol {
  implicit val _ = jsonFormat3(Talk.apply)
}

case class Place(title: String, lat: Double, lon: Double, address: String, description: String)

object Place extends DefaultJsonProtocol {
  implicit val _ = jsonFormat5(Place.apply)
}

case class Event(
  id: String,
  talksPlace: Place, 
  beersPlace: Place,
  talks: List[Talk], 
  lightningTalks: List[Talk], 
  time: Long, 
  participants: List[User]
)

object Event extends DefaultJsonProtocol {
  implicit val _ = jsonFormat7(Event.apply)

  def apply(id: String, talksPlace: Place, beersPlace: Place, talks: List[Talk], lightningTalks: List[Talk], time: Long): Event = {
    val speakers = (talks.map(_.speaker) ::: lightningTalks.map(_.speaker)).distinct
    Event(id, talksPlace, beersPlace, talks, lightningTalks, time, speakers)
  }
}

case class Events(events: List[Event])

object Events extends DefaultJsonProtocol {
  implicit val _ = jsonFormat1(Events.apply)
}

case class Tweet(id: Long, creationTime: Long, place: Option[Place], text: String, user: User)

object Tweet extends DefaultJsonProtocol {
  implicit val _ = jsonFormat5(Tweet.apply)
}

case class Tweets(tweets: List[Tweet])

object Tweets extends DefaultJsonProtocol {
  implicit val _ = jsonFormat1(Tweets.apply)
}
