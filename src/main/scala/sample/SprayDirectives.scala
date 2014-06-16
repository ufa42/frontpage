package sample

import akka.actor.{ActorLogging, Actor, Props, ActorSystem}
import akka.io.IO
import scala.concurrent.duration._
import spray.can.Http
import spray.client.pipelining._
import spray.http._
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling._
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
import spray.routing.HttpService
import scala.util.Random
import java.util.UUID


class HttpServiceActor extends Actor with HttpService with ActorLogging {

  implicit def executionContext = actorRefFactory.dispatcher

  def actorRefFactory = context
  def receive = runRoute(route)

  var event = Event(
    "140619",
    Place("ШБ Синергия", 54.7252452, 55.949416, "Уфа, ул. Коммунистическая, 54", ""),
    Place("Дом №50", 54.726987, 55.940932, "Уфа, ул. Коммунистическая, 50", "4 этаж"),
    List(
      Talk("Альфа версия сайта знакомств за 6 месяцев - работа над ошибками",
        "Some description",
        User(16135676, "@abdullin", "Rinat Abdullin", "http://pbs.twimg.com/profile_images/3479036762/40c99d96aa9a4e57cfa7d54d1fb7d5b2.jpeg")
      ),
      Talk("Опыт использования Scala для разработки соцсети",
        "Some description",
        User(123111, "@levkhomich", "Lev Khomich", "https://pbs.twimg.com/profile_images/459340276188708864/b3X4WwoB.png")
      )
    ),
    List(
      Talk("Emacs крут",
        "Some description",
        User(16135676, "@abdullin", "Rinat Abdullin", "http://pbs.twimg.com/profile_images/3479036762/40c99d96aa9a4e57cfa7d54d1fb7d5b2.jpeg")
      ),
      Talk("Objective-C Runtime – вскрытие без наркоза",
        "Some description",
        User(12312312, "@MrDarK_AngeL", "Rishat Shamsutdinov", "https://pbs.twimg.com/profile_images/2187811129/image.jpg")
      ),
      Talk("Как быстро написать приложение на angular.js? Не писать на angular.js",
        "Some description",
        User(16781, "no twitter", "Grigory Leonenko", "https://pp.vk.me/c613522/v613522262/f295/WXWyojalNxo.jpg")
      ),
      Talk("MongoDB - PHP",
        "Some description",
        User(654, "no twitter", "Alexey Kardapoltsev", "https://lh6.googleusercontent.com/-sugMcSpyotA/AAAAAAAAAAI/AAAAAAAAAno/Q5uCER67CnM/s120-c/photo.jpg")
      ),
      Talk("Нужно ли реализовывать жизненный цикл для данных?",
        "Some description",
        User(412, "no twitter", "Anjei Katkov", "assets/img/logo.png")
      )
    ),
    DateTime(2014, 6, 19, 19 - 6).clicks
  )
  def events = List(event).sortBy(_.time)

  val twitterBot = new TwitterBot("nnrqExJROGCpBglV5xazTtzQ4", "rXLGuo1fSkHPEugIpDL2aeUSOjUASXEimjsSbLSU2ic3iHNqPD", context.system.scheduler)
  twitterBot.addSubscription("#ufa42")

  context.system.scheduler.schedule(5.seconds, 2.seconds, new Runnable {
    override def run(): Unit = {
      val newParticipants = twitterBot.tweets.map(_.user.id).toSet -- event.participants.map(_.id)
      event = event.copy(participants = twitterBot.fetchUsers(newParticipants) ::: event.participants)
    }
  })

  val route = {
    (get & path("")) {
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
        path("tweets") {
          complete(Tweets(twitterBot.tweets))
        }
      }
    } ~
    get {
      path("assets" / Rest) { case (path) =>
        getFromResource("assets/" + path)
      }
    }
  }

}

object SprayDirectives extends App {
  val host = "0.0.0.0"
  val port = 8889

  implicit val system = ActorSystem("ufa42")
  val service = system.actorOf(Props[HttpServiceActor])
  IO(Http) ! Http.Bind(service, host, port)
  system.awaitTermination()
}
