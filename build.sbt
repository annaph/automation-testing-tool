name := "cartagena-tool"

organization := "org.cartagena.tool"

version := "1.1.17-SNAPSHOT"

scalaVersion := "2.12.10"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-language:implicitConversions",
  "-language:reflectiveCalls")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.12.10",
  "org.scalaz" %% "scalaz-core" % "7.2.28",
  "org.scalaz" %% "scalaz-effect" % "7.2.28",
  "org.apache.httpcomponents" % "httpclient" % "4.5.10",
  "org.json4s" %% "json4s-jackson" % "3.6.7",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "org.mockito" % "mockito-core" % "3.1.0" % Test)

fork := true
