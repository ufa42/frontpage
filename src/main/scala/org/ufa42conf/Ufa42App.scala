package org.ufa42conf

import akka.actor.{ActorLogging, Actor, Props, ActorSystem}
import akka.io.IO
import scala.concurrent.duration._
import spray.can.Http
import spray.http._
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling._
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
import spray.routing.HttpService


class HttpServiceActor extends Actor with HttpService with ActorLogging {

  import ConfEvents._

  implicit def executionContext = actorRefFactory.dispatcher

  def actorRefFactory = context
  def receive = runRoute(route)

  var attendees = Map[Event, Set[User]]().withDefaultValue(Set.empty)
  var events = List[Event]()

  var polls = List[PollResult]()

  val twitterBot = new TwitterBot("nnrqExJROGCpBglV5xazTtzQ4", "rXLGuo1fSkHPEugIpDL2aeUSOjUASXEimjsSbLSU2ic3iHNqPD", context.system.scheduler)

  twitterBot.addSubscription("#ufa42")
  addEvent(event0)
  addEvent(event1)
  addEvent(event2)
  addEvent(event3)

  val intentPatterns =
    """\bсоби?ра,\b(за|подо?|при?|до|по)?[ийе]д[уеё]\b,идти,\bбуду\b,бы(ва)?ть\b,\bвизит,\bпосещ,
      |слуша,гости,\bзагляну,\bвстре(ти|ч),\b(у,с)видимся,
      |\bgo,\bvisit,\bmeet,\bsee,\bwill,\battend""".stripMargin.split(',').toList.map(".*" + _ + ".*")

  context.system.scheduler.schedule(5.seconds,4.seconds, new Runnable {
    override def run(): Unit = {
      val intentTweets = twitterBot.tweets.
        filter { t =>
          intentPatterns.find(p => t.text.toLowerCase.matches(".*" + p + ".*")) match {
            case Some(p) =>
              !t.text.toLowerCase.matches(".*\\b(не|not)[ ]+" + p.substring(2))
            case _ =>
              false
          }
        }.filterNot(_.user.name.endsWith("ufa42conf"))
      events.sliding(2).foreach { case List(event, prevEvent) =>
        intentTweets.filter(t => t.creationTime > prevEvent.time && t.creationTime < event.time).map(_.user) match {
          case Nil =>
          case newAttendees =>
            try {
              addAttendees(event, newAttendees.toSet)
            } catch {
              case t: Throwable =>
                // ignore it
                t.printStackTrace()
            }
        }
      }
    }
  })

  def addAttendees(event: Event, newAttendees: Set[User]): Unit = {
    newAttendees.filterNot(attendees(event).contains) match {
      case s if s.isEmpty =>
      case delta =>
        println(s"new attendees for event ${event.id}: " + delta.map(_.name))
        attendees = attendees.updated(event, attendees(event) ++ newAttendees)
    }
  }

  def addEvent(event: Event): Unit = {
    val speakers = event.talks.map(_.speaker) ::: event.lightningTalks.map(_.speaker)
    addAttendees(event, speakers.toSet)
    events = (event :: events).sortBy(-_.time)
  }


  def detectIOS: HttpHeader => Option[Boolean] = {
    case h: HttpHeaders.`User-Agent` => Some(h.value.contains("iPhone") || h.value.contains("iPad") || h.value.contains("iPod"))
    case _ => None
  }

  def savePoll(poll: PollResult): StatusCode = {
    polls ::= poll
    StatusCodes.OK
  }

  val route = dynamic {
    (get & path("") & headerValue(detectIOS)) { iOS =>
      if (iOS)
        getFromResource("assets/index.ios.html")
      else
        getFromResource("assets/index.html")
    } ~
    pathPrefix("api") {
      get {
        path("events") {
          complete(Events(
            events.map(event => EventDto(event, attendees(event)))
          ))
        } ~
        path("events" / Segment) { case id =>
          events.find(_.id == id) match {
            case Some(found) =>
              complete(found)
            case _ =>
              complete(HttpResponse(StatusCodes.NotFound))
          }
        } ~
        (path("tweets") & parameter('limit.as[Int].?)) { limit =>
          complete(Tweets(twitterBot.tweets.takeRight(limit.getOrElse(3))))
        } ~
        path("polls---123") {
          complete(polls)
        }
      } ~
      (path("poll") & post) {
        handleWith(savePoll)
      }
    } ~
    get {
      path("assets" / Rest) { case (path) =>
        getFromResource("assets/" + path)
      }
    }
  }

}

object Ufa42App extends App {
  val host = "0.0.0.0"
  val port = 8889

  implicit val system = ActorSystem("ufa42")
  val service = system.actorOf(Props[HttpServiceActor])
  IO(Http) ! Http.Bind(service, host, port)
  system.awaitTermination()
}
