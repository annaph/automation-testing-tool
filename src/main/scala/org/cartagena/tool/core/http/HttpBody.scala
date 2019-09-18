package org.cartagena.tool.core.http

import com.fasterxml.jackson.databind.ObjectMapper

import scala.util.{Failure, Success, Try}

sealed trait HttpBody extends Any {

  def isValid: Boolean =
    true

  def toPrettyString: String =
    HttpBody.toPrettyString(this)

}

case class Text(str: String) extends AnyVal with HttpBody

case class JsonString(str: String) extends HttpBody {

  import HttpBody.JSON_STRING_INVALID_ERR_MSG

  require(isValid, JSON_STRING_INVALID_ERR_MSG)

  private lazy val _prettyString = super.toPrettyString

  override def toPrettyString: String =
    _prettyString

  override def isValid: Boolean =
    Try {
      _prettyString
    } match {
      case Success(_) =>
        true
      case Failure(e) =>
        false
    }

}

case object Empty extends HttpBody

object HttpBody {

  private[http] val EMPTY_BODY_CONTENT = "<empty>"

  private[http] val JSON_STRING_INVALID_ERR_MSG = "Json string invalid!"

  private val _mapper: ObjectMapper = new ObjectMapper()

  private def toPrettyString(httpBody: HttpBody): String =
    httpBody match {
      case Text(str) =>
        str
      case x: JsonString =>
        prettifyJsonString(x)
      case Empty =>
        EMPTY_BODY_CONTENT
    }

  private def prettifyJsonString(jsonString: JsonString): String = {
    val jsonObject = _mapper readValue(jsonString.str, classOf[Object])
    _mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject)
  }

}
