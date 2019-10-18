package org.cartagena.tool.core.model

import java.util.Properties

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

trait Profile {

  private lazy val _properties: Map[String, String] =
    readProperties

  def name: String

  def path: String

  def getProperty(key: String): Option[String] =
    _properties get key

  def getAllProperties: Map[String, String] =
    _properties

  protected def readProperties: Map[String, String]

  protected def readPropertyFile(fileName: String): Map[String, String] =
    Profile readPropertyFile (path + fileName + ".properties")

}

trait DefaultProfile extends Profile {

  override def name: String = Profile.DEFAULT_PROFILE_NAME

  override protected def readProperties: Map[String, String] =
    readPropertyFile("default")

}

trait EmptyProfile extends Profile {

  override val name: String = Profile.EMPTY_PROFILE_NAME

  override val path: String = ""

  override protected def readProperties: Map[String, String] =
    Map.empty

}

object Profile {

  val DEFAULT_PROFILE_NAME = "default-profile"

  val EMPTY_PROFILE_NAME = "empty-profile"

  private def readPropertyFile(filePath: String): Map[String, String] = {
    Try {
      val props = new Properties()
      props load this.getClass.getResourceAsStream(filePath)
      props.asScala
    } match {
      case Success(properties) =>
        properties.toMap
      case Failure(e) =>
        throw new Exception(s"Error reading property file '$filePath'", e)
    }
  }

}
