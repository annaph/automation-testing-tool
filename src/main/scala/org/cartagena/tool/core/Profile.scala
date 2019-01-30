package org.cartagena.tool.core

import java.util.Properties

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

trait ProfileLocation {
  def path: String
}

trait Profile {
  self: ProfileLocation =>

  private lazy val _properties: Map[String, String] =
    readProperties

  def name: String

  def getProperty(key: String): Option[String] =
    _properties get key

  def getAllProperties: Map[String, String] = _properties

  def readPropertyFile(fileName: String): Map[String, String] =
    Profile.readPropertyFile(location + fileName)

  def location: String = self.path

  protected def readProperties: Map[String, String]

}

abstract class DefaultProfile extends Profile {
  self: ProfileLocation =>

  override def name: String = "default-profile"

  override def readProperties: Map[String, String] =
    Option(getClass.getResource(location + "default.properties")) match {
      case Some(_) =>
        readPropertyFile("default")
      case None =>
        Map.empty
    }
}

object Profile {

  private def readPropertyFile(filePath: String): Map[String, String] = {
    Try {
      val props = new Properties()
      props load this.getClass.getResourceAsStream(filePath + ".properties")
      props.asScala
    } match {
      case Success(properties) =>
        properties.toMap
      case Failure(e) =>
        throw new Exception(s"Error reading property file '$filePath'", e)
    }
  }

}
