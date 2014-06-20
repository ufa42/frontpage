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

  implicit def executionContext = actorRefFactory.dispatcher

  def actorRefFactory = context
  def receive = runRoute(route)

  var event = Event(
    "140619",
    Place("ШБ Синергия", 54.7252452, 55.949416, "Уфа, ул. Коммунистическая, 54", ""),
    Place("Дуслык", 54.7276034, 55.9494373, "Уфа, ул. Крупской, 9", "2 этаж"),
    List(
      Talk("Альфа версия сайта знакомств за 6 месяцев - работа над ошибками",
        "",
        User(16135676L, "@abdullin", "Rinat Abdullin", Some("http://pbs.twimg.com/profile_images/3479036762/40c99d96aa9a4e57cfa7d54d1fb7d5b2.jpeg"))
      ),
      Talk("Почему мы используем Scala?",
        "",
        User(2378268950L, "@andrey_feokt", "Андрей Феоктистов", Some("https://pbs.twimg.com/profile_images/478884565369360384/RevpRhzK.png"))
      ),
      Talk("HTTP слой со Spray и Akka",
        "",
        User(9600972L, "@levkhomich", "Lev Khomich", Some("https://pbs.twimg.com/profile_images/459340276188708864/b3X4WwoB.png")),
        "assets/talks/spray/spray-intro.html"
      )
    ),
    List(
      Talk("Emacs крут",
        "",
        User(16135676L, "@abdullin", "Rinat Abdullin", Some("http://pbs.twimg.com/profile_images/3479036762/40c99d96aa9a4e57cfa7d54d1fb7d5b2.jpeg"))
      ),
      Talk("Objective-C Runtime – вскрытие без наркоза",
        "",
        User(94962222L, "@MrDarK_AngeL", "Rishat Shamsutdinov", Some("https://pbs.twimg.com/profile_images/2187811129/image.jpg"))
      ),
      Talk("Как быстро написать приложение на angular.js? Не писать на angular.js",
        "",
        User(1, "no twitter", "Grigory Leonenko", Some("https://pp.vk.me/c613522/v613522262/f295/WXWyojalNxo.jpg"))
      ),
//      Talk("MongoDB - PHP",
//        "",
//        User(654, "no twitter", "Alexey Kardapoltsev", "https://lh6.googleusercontent.com/-sugMcSpyotA/AAAAAAAAAAI/AAAAAAAAAno/Q5uCER67CnM/s120-c/photo.jpg")
//      ),
      Talk("Нужно ли реализовывать жизненный цикл для данных?",
        "",
        User(2, "no twitter", "Anjei Katkov", None)
      ),
      Talk("Особенности интернационализации SPA (single page applications)",
        "",
        User(568182702L, "izuick", "Ruslan Zuick", Some("https://pbs.twimg.com/profile_images/2181307609/IMG_10832.gif"))
      )
    ),
    DateTime(2014, 6, 19, 19 - 6).clicks,
    List(
      Question(null, "Оцените уровень проведения конференции",
        List(Answer(1, "отлично"), Answer(2, "хорошо"), Answer(3, "нормально"), Answer(4, "плохо")), 1)
    )
  )
  def events = List(event).sortBy(_.time)

  var polls = List[PollResult]()

  val twitterBot = new TwitterBot("nnrqExJROGCpBglV5xazTtzQ4", "rXLGuo1fSkHPEugIpDL2aeUSOjUASXEimjsSbLSU2ic3iHNqPD", context.system.scheduler)
  twitterBot.addSubscription("#ufa42")

  val intentPatterns =
    """\bсоби?ра,\b(за|подо?|при?|до|по)?[ийе]д[уеё]\b,идти,\bбуду\b,бы(ва)?ть\b,\bвизит,\bпосещ,слуша,гости,\bзагляну,
      |\bgo,\bvisit,\bmeet,\bsee,\bwill,\battend""".stripMargin.split(',').toList.map(".*" + _ + ".*")

  context.system.scheduler.schedule(5.seconds,4.seconds, new Runnable {
    override def run(): Unit = {
      val intentTweets = twitterBot.tweets.
        filter { t =>
          intentPatterns.find(p => t.text.toLowerCase.matches(".*" + p + ".*")) match {
            case Some(p) =>
//              !Pattern.compile(p).matcher(t.text.toLowerCase).replaceAll("$").contains("не $")
              !t.text.toLowerCase.matches(".*\\b(не|not)[ ]+" + p.substring(2))
            case _ =>
              false
          }
        }.filterNot(_.user.name.endsWith("ufa42conf"))
      val newParticipants = intentTweets.map(_.user.id).toSet -- event.participants.map(_.id)
      if (!newParticipants.isEmpty) {
        println("new participants: " + newParticipants)
        try {
          event = event.copy(participants = twitterBot.fetchUsers(newParticipants) ::: event.participants)
        } catch {
          case t: Throwable =>
            // ignore it
            t.printStackTrace()
        }
      }
    }
  })

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
          complete(Events(events))
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
