name := "akka esb"

scalaVersion := "2.10.1"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test"

libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"

libraryDependencies += "com.rabbitmq" % "amqp-client" % "3.0.4"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
 
libraryDependencies +=  "com.typesafe.akka" %% "akka-actor" % "2.1.2"



