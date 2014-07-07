package org.ufa42conf

import spray.http.DateTime

object ConfEvents {
  var event0 = Event(
    "140610",
    Place("ШБ Синергия", 54.7252452, 55.949416, "Уфа, ул. Коммунистическая, 54", ""),
    Place("Дуслык", 54.7276034, 55.9494373, "Уфа, ул. Крупской, 9", "2 этаж"),
    Nil, Nil, DateTime(2014, 6, 10, 19 - 6).clicks, Nil
  )

  var event1 = Event(
    "140619",
    Place("ШБ Синергия", 54.7252452, 55.949416, "Уфа, ул. Коммунистическая, 54", ""),
    Place("Дуслык", 54.7276034, 55.9494373, "Уфа, ул. Крупской, 9", "2 этаж"),
    List(
      Talk("Альфа версия сайта знакомств за 6 месяцев - работа над ошибками",
        "",
        User(16135676L, "abdullin", "Rinat Abdullin", Some("http://pbs.twimg.com/profile_images/3479036762/40c99d96aa9a4e57cfa7d54d1fb7d5b2.jpeg"))
      ),
      Talk("Почему мы используем Scala?",
        "",
        User(2378268950L, "andrey_feokt", "Андрей Феоктистов", Some("https://pbs.twimg.com/profile_images/478884565369360384/RevpRhzK.png"))
      ),
      Talk("HTTP слой со Spray и Akka",
        "",
        User(9600972L, "levkhomich", "Lev Khomich", Some("https://pbs.twimg.com/profile_images/459340276188708864/b3X4WwoB.png")),
        "assets/talks/spray/spray-intro.html"
      )
    ),
    List(
      Talk("Emacs крут",
        "",
        User(16135676L, "abdullin", "Rinat Abdullin", Some("http://pbs.twimg.com/profile_images/3479036762/40c99d96aa9a4e57cfa7d54d1fb7d5b2.jpeg"))
      ),
      Talk("Objective-C Runtime – вскрытие без наркоза",
        "",
        User(94962222L, "MrDarK_AngeL", "Rishat Shamsutdinov", Some("https://pbs.twimg.com/profile_images/2187811129/image.jpg"))
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
      Question("140619-1", "Оцените уровень проведения конференции",
        List(Answer(1, "отлично"), Answer(2, "хорошо"), Answer(3, "нормально"), Answer(4, "плохо")), 1)
    )
  )
  var event2 = Event(
    "140710",
    Place("ШБ Синергия", 54.7252452, 55.949416, "Уфа, ул. Коммунистическая, 54", ""),
    Place("Дуслык", 54.7276034, 55.9494373, "Уфа, ул. Крупской, 9", "2 этаж"),
    List(
      Talk("Отладка распределенных систем",
        "",
        User(9600972L, "levkhomich", "Lev Khomich", Some("https://pbs.twimg.com/profile_images/459340276188708864/b3X4WwoB.png"))
      ),
      Talk("Распределенные event-driven системы",
        "",
        User(16135676L, "abdullin", "Rinat Abdullin", Some("http://pbs.twimg.com/profile_images/3479036762/40c99d96aa9a4e57cfa7d54d1fb7d5b2.jpeg"))
      )
    ),
    List(
      Talk("Docker - пакуем приложения",
        "",
        User(16135676L, "abdullin", "Rinat Abdullin", Some("http://pbs.twimg.com/profile_images/3479036762/40c99d96aa9a4e57cfa7d54d1fb7d5b2.jpeg"))
      ),
      Talk("Vim - в чем фишка",
        "",
        User(1, "avpxalive", "Artem Popov", Some("https://pbs.twimg.com/profile_images/2470048812/fee2031e43a59d4d4fd583b9b34aa4dc.jpeg"))
      ),
      Talk("iOS: не используйте Storyboard",
        "",
        User(94962222L, "MrDarK_AngeL", "Rishat Shamsutdinov", Some("https://pbs.twimg.com/profile_images/2187811129/image.jpg"))
      ),
      Talk("Jira, тяжелая артиллерия энтерпрайза в стартапе",
        "",
        User(39486845L, "akitka", "Ksenia", Some("https://pbs.twimg.com/profile_images/476727113383297024/hJMp-Xxw.jpeg"))
      )
    ),
    DateTime(2014, 7, 10, 19 - 6).clicks,
    Nil
  )
}
