name := "smokaz"

scalaVersion :=  "2.11.2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.4"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.3.4"

libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.3.4"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1-M3"

libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5", "org.slf4j" % "slf4j-simple" % "1.7.5")


