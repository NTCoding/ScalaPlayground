name := "Atmosphere Scala"

scalaVersion := "2.10.2"

libraryDependencies += "org.atmosphere" % "atmosphere-runtime" % "2.0.0.RC3"

libraryDependencies += "javax.servlet" % "servlet-api" % "2.5"

libraryDependencies += "org.mortbay.jetty" % "servlet-api" % "3.0.20100224"

libraryDependencies += "eu.infomas" % "annotation-detector" % "3.0.0"

libraryDependencies += "org.atmosphere" % "atmosphere-annotations" % "2.0.0.RC3"

libraryDependencies += "org.mortbay.jetty" % "jetty" % "6.1.22" % "container"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.5"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.2.0"
            
classDirectory in Compile := file("src/main/webapp/WEB-INF/classes")

seq(webSettings :_*)