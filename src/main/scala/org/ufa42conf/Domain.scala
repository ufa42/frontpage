package org.ufa42conf

import spray.json.DefaultJsonProtocol


case class User(id: Long, name: String, fullname: String, avatarUrl: Option[String]) {
  override def hashCode(): Int = id.hashCode()
  override def equals(obj: scala.Any): Boolean =
    obj match {
      case that: User => that.id == id
      case _ => false
    }
}

object User extends DefaultJsonProtocol {
  implicit val _ = jsonFormat4(User.apply)

  def apply(user: twitter4j.User): User =
    User(user.getId, user.getScreenName, user.getName, Some(user.getOriginalProfileImageURLHttps))
}

case class Talk(title: String, description: String, speaker: User, slidesUrl: String = "#")

object Talk extends DefaultJsonProtocol {
  implicit val _ = jsonFormat4(Talk.apply)
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

case class PollResult(answers: Map[String, String])

object PollResult extends DefaultJsonProtocol {
  implicit val _ = jsonFormat1(PollResult.apply)
}

case class Event(
  id: String,
  talksPlace: Place, 
  beersPlace: Place,
  talks: List[Talk], 
  lightningTalks: List[Talk], 
  time: Long,
  questions: List[Question]
)

object Event extends DefaultJsonProtocol {
  implicit val _ = jsonFormat7(Event.apply)
}

case class EventDto(
  id: String,
  talksPlace: Place,
  beersPlace: Place,
  talks: List[Talk],
  lightningTalks: List[Talk],
  time: Long,
  attendees: Set[User],
  questions: List[Question]
)

object EventDto extends DefaultJsonProtocol {
  implicit val _ = jsonFormat8(EventDto.apply)

  def apply(event: Event, attendees: Set[User]): EventDto =
    EventDto(event.id, event.talksPlace, event.beersPlace, event.talks, event.lightningTalks, event.time, attendees, event.questions)
}

case class Events(events: List[EventDto])

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
        Place(place.getName, t.getGeoLocation.getLatitude, t.getGeoLocation.getLongitude, place.getFullName, "")
      ),
      t.getText,
      User(t.getUser)
    )

}

case class Tweets(tweets: List[Tweet])

object Tweets extends DefaultJsonProtocol {
  implicit val _ = jsonFormat1(Tweets.apply)
}
