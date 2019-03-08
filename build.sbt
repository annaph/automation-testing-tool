name := "cartagena-tool"

organization := "org.cartagena.tool"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-language:implicitConversions")

libraryDependencies ++= Seq(
  "org.apache.httpcomponents" % "httpclient" % "4.5.7",
  "commons-io" % "commons-io" % "2.6",
  "org.json4s" %% "json4s-jackson" % "3.6.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test)

fork := true
