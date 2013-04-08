import sbt._
import sbt.Keys._

object AkkaProjectInScalaBuild extends Build {

  lazy val akkaProjectInScala = Project(
    id = "akka-project-in-scala",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Akka Project In Scala",
      organization := "akka tutorial",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.2",
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.1"
    )
  )
}
