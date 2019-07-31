name := "cartagena-tool"

organization := "org.cartagena.tool"

version := "1.1.10-SNAPSHOT"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-language:implicitConversions",
  "-language:reflectiveCalls")

libraryDependencies ++= Seq(
  "org.apache.httpcomponents" % "httpclient" % "4.5.7",
  "commons-io" % "commons-io" % "2.6",
  "org.json4s" %% "json4s-jackson" % "3.6.5",
  "org.scalaz" %% "scalaz-core" % "7.2.27",
  "org.scalaz" %% "scalaz-effect" % "7.2.27",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.mockito" % "mockito-core" % "2.25.1" % Test)

fork := true
