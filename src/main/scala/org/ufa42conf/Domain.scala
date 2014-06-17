package org.ufa42conf

import spray.json.DefaultJsonProtocol


case class User(id: Long, name: String, fullname: String, avatarUrl: Option[String])

object User extends DefaultJsonProtocol {
  implicit val _ = jsonFormat4(User.apply)

  def apply(user: twitter4j.User): User =
    User(user.getId, user.getScreenName, user.getName, Some(user.getProfileImageURLHttps))
}

case class Talk(title: String, description: String, speaker: User)

object Talk extends DefaultJsonProtocol {
  implicit val _ = jsonFormat3(Talk.apply)
}

case class Place(title: String, lat: Double, lon: Double, address: String, description: String)

object Place extends DefaultJsonProtocol {
  implicit val _ = jsonFormat5(Place.apply)
}

case class Answer(id: Long, text: String)

object Answer extends DefaultJsonProtocol {
  implicit val _ = jsonFormat2(Answer.apply)
}

case class Question(id: String, text: String, answers: List[Answer], answerLimit: Int)

object Question extends DefaultJsonProtocol {
  implicit val _ = jsonFormat4(Question.apply)
}



case class Event(
  id: String,
  talksPlace: Place, 
  beersPlace: Place,
  talks: List[Talk], 
  lightningTalks: List[Talk], 
  time: Long, 
  participants: List[User],
  questions: List[Question]
)

object Event extends DefaultJsonProtocol {
  implicit val _ = jsonFormat8(Event.apply)

  def apply(id: String, talksPlace: Place, beersPlace: Place, talks: List[Talk], lightningTalks: List[Talk], time: Long, questions: List[Question]): Event = {
    val speakers = (talks.map(_.speaker) ::: lightningTalks.map(_.speaker)).distinct
    Event(id, talksPlace, beersPlace, talks, lightningTalks, time, speakers,
      questions.zipWithIndex.map { case (q, i) => q.copy(id = id + '-' + i) })
  }
}

case class Events(events: List[Event])

object Events extends DefaultJsonProtocol {
  implicit val _ = jsonFormat1(Events.apply)
}

case class Tweet(id: Long, creationTime: Long, place: Option[Place], text: String, user: User)

object Tweet extends DefaultJsonProtocol {
  implicit val _ = jsonFormat5(Tweet.apply)

  def apply(t: twitter4j.Status): Tweet =
    Tweet(
      t.getId,
      t.getCreatedAt.getTime,
      Option(t.getPlace).map(place =>
        Place(place.getName, t.getGeoLocation.getLatitude, t.getGeoLocation.getLongitude, place.getStreetAddress, "")
      ),
      t.getText,
      User(t.getUser)
    )

}

case class Tweets(tweets: List[Tweet])

object Tweets extends DefaultJsonProtocol {
  implicit val _ = jsonFormat1(Tweets.apply)
}
