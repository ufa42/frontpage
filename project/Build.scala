import sbt._
import Keys._

object ProjectBuild extends Build {

  lazy val root = Project(

    id = "ufa42",
    base = file("."),
    settings =
      Defaults.defaultSettings ++
      Seq (
        name := "ufa42",
        organization := "org.ufa42conf",
        version := "0.1",

        scalaVersion := "2.10.4",
        scalacOptions in GlobalScope ++= Seq("-Xcheckinit", "-Xlint", "-deprecation", "-unchecked", "-feature", "-language:_"),
        scalacOptions in Test ++= Seq("-Yrangepos"),

        publish := (),

        resolvers += "Maven Central Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
        libraryDependencies ++=
          Seq(
            "io.spray" % "spray-can" % "1.3.1",
            "io.spray" % "spray-routing" % "1.3.1",
            "io.spray" % "spray-client" % "1.3.1",
            "io.spray" %% "spray-json" % "1.2.6",
	          "org.twitter4j" % "twitter4j-core" % "4.0.1",
            "com.typesafe" % "config" % "1.2.0",
            "com.typesafe.akka" %% "akka-actor" % "2.3.2"
          )
      )
  )
}