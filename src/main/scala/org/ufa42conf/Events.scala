package org.ufa42conf

import spray.http.DateTime

object ConfEvents {
  val rinat = User(16135676L, "abdullin", "Rinat Abdullin", Some("http://pbs.twimg.com/profile_images/3479036762/40c99d96aa9a4e57cfa7d54d1fb7d5b2.jpeg"))
  val lev = User(9600972L, "levkhomich", "Lev Khomich", Some("https://pbs.twimg.com/profile_images/459340276188708864/b3X4WwoB.png"))
  val andrey = User(2378268950L, "andrey_feokt", "Andrey Feoktistov", Some("https://pbs.twimg.com/profile_images/478884565369360384/RevpRhzK.png"))
  val rishat = User(94962222L, "MrDarK_AngeL", "Rishat Shamsutdinov", Some("https://pbs.twimg.com/profile_images/507544081548206080/VJTYy-dc_400x400.jpeg"))
  val grisha = User(1, "no twitter", "Grigory Leonenko", Some("https://pp.vk.me/c613522/v613522262/f295/WXWyojalNxo.jpg"))
  val anjei = User(2, "no twitter", "Anjei Katkov", None)
  val ruslan = User(568182702L, "izuick", "Ruslan Zuick", Some("https://pbs.twimg.com/profile_images/512286625305358336/q49k5rcd.jpeg"))
  val artem = User(3, "avpxalive", "Artem Popov", Some("https://pbs.twimg.com/profile_images/2470048812/fee2031e43a59d4d4fd583b9b34aa4dc.jpeg"))
  val ksenia = User(39486845L, "akitka", "Ksenia Makarova", Some("https://pbs.twimg.com/profile_images/476727113383297024/hJMp-Xxw.jpeg"))
  val fil = User(4, "lozga", "Filipp Terekhov", Some("https://pbs.twimg.com/profile_images/378800000802397697/a63d690bdef1a580e14ca4ee48fdc878_400x400.png"))
  val h3m0ptys1s = User(5, "h3m0ptys1s", "Oleg Gumerov", Some("https://pbs.twimg.com/profile_images/553508290261311489/EOUgvLll_400x400.jpeg"))
  val sergey = User(6, "krasina15", "Sergey Tarasenko", Some("https://pbs.twimg.com/profile_images/915874529/f8376ccebd0220635cc7c8924804b120_400x400.png"))
  val arsenij = User(7, "no twitter", "Arsenij Imamutdinov", Some("http://s018.radikal.ru/i511/1504/b6/fb5100a0446a.jpg"))
  val nikolay = User(8, "NikolayIakovlev", "Nikolay Iakovlev", Some("https://pbs.twimg.com/profile_images/2159744451/appleStore.jpg"))

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
      Talk("Альфа версия сайта знакомств за 6 месяцев - работа над ошибками", "", rinat),
      Talk("Почему мы используем Scala?", "", andrey),
      Talk("HTTP слой со Spray и Akka", "", lev, "assets/talks/spray/spray-intro.html")
    ),
    List(
      Talk("Emacs крут", "", rinat),
      Talk("Objective-C Runtime – вскрытие без наркоза", "", rishat),
      Talk("Как быстро написать приложение на angular.js? Не писать на angular.js", "", grisha),
      Talk("Нужно ли реализовывать жизненный цикл для данных?", "", anjei),
      Talk("Особенности интернационализации SPA (single page applications)", "", ruslan)
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
      Talk("Отладка распределенных систем", "", lev, "assets/talks/dds/dds.html"),
      Talk("5 Event-driven лайфхаков для вашего кода", "", rinat)
    ),
    List(
      Talk("Мобильное приложение для управления презентацией за 30 минут", "", rinat),
      Talk("Vim - в чем фишка", "", artem),
      Talk("iOS: не используйте Storyboard", "", rishat),
      Talk("Jira, тяжелая артиллерия энтерпрайза в стартапе", "", ksenia)
    ),
    DateTime(2014, 7, 10, 19 - 6).clicks,
    Nil
  )
  var event3 = Event(
    "150225",
    Place("ШБ Синергия", 54.7252452, 55.949416, "Уфа, ул. Коммунистическая, 54", ""),
    Place("Гости", 54.719282, 55.949928, "Уфа, ул. Цюрупы, 12", ""),
    List(
      Talk("Cвет в конце тоннеля - ReactJS", "", rinat),
      Talk("Переход с c* на riak", "", lev, "assets/talks/migration/migration.html"),
      Talk("Objective-C Runtime: немного теории и практическое применение", "", rishat, "assets/talks/swizzling.pdf")
    ),
    List(
      Talk("Чем хорош Sikuli (кроме названия)", "", fil, "assets/talks/Sikuli.odp"),
      Talk("Из чего складывается user experience", "", h3m0ptys1s, "assets/talks/UX.ppt"),
      Talk("\"Hello World\" на микросхеме", "", rinat),
      Talk("Переход на cqrs и контекстное кэширование", "", lev, "assets/talks/cqrs/cqrs.html"),
      Talk("Доставить за 60 миллисекунд", "", sergey, "assets/talks/CDN.pdf")
    ),
    DateTime(2015, 2, 25, 19 - 5).clicks,
    Nil
  )
  var event4 = Event(
    "150427",
    Place("ШБ Синергия", 54.7252452, 55.949416, "Уфа, ул. Коммунистическая, 54", ""),
    Place("Morris", 54.728811, 55.941556, "Уфа, ул. Гоголя 60/1", ""),
    List(
      Talk("React Native глазами не iOS разработчика", "", rinat),
      Talk("React Native глазами iOS разработчика", "", rishat),
      Talk("Разработка Android-приложений на Scala", "", anjei),
      Talk("Аутсорсинг разработки ПО", "", h3m0ptys1s),
      Talk("TopCoder: риск - дело благородное", "", nikolay),
      Talk("Apache Spark: как перестать беспокоиться и начать жить", "", lev)
    ),
    List(
      Talk("Телеметрия в .NET", "", rinat),
      Talk("“Секционирование” БД, как не надо делать", "", arsenij),
      Talk("Немного о средствах профилирования производительности в Chrome", "", ruslan)
    ),
    DateTime(2015, 4, 27, 19 - 5).clicks,
    Nil
  )
}
