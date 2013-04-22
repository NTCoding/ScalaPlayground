name := "weKanban"

organization := "scalainaction"

version := "0.1"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq(
    "org.scalaz" %% "scalaz-core" % scalazVersion,
    "org.scalaz" %% "scalaz-http" % scalazVersion,
    "org.eclipse.jetty" % "jetty-servlet" % jettyVersion % "container",
    "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "test, container",
    "org.eclipse.jetty" % "jetty-server" % jettyVersion % "container",
    "com.h2database" % "h2" % "1.2.137",
    "org.squeryl" % "squeryl_2.10" % "0.9.5-6"
)

seq(com.github.siasia.WebPlugin.webSettings :_*)
