package org.cartagena.tool.core.http.apache

import org.apache.http.{Header, HeaderElement}
import org.cartagena.tool.core.http.apache.ApacheHttpResponse.COOKIE_HEADER_NAME

object ApacheHttpTestUtil {

  val PROTOCOL = "HTTP"
  val PROTOCOL_VERSION = 1

  val STATUS_CODE_200 = 200
  val STATUS_CODE_201 = 201
  val STATUS_CODE_204 = 204

  val REASON_PHRASE_OK = "OK"
  val REASON_PHRASE_CREATED = "Created"
  val REASON_PHRASE_NO_CONTENT = "No Content"

  val URL_STRING = "http://www.google.com/"
  val HEADER: (String, String) = "header1" -> "value1"
  val PARAM: (String, String) = "param1" -> "value1"
  val BODY_CONTENT = "This is some body"

  val COOKIE_HEADER_VALUE = "cookie-header-value"

  val COOKIE_NAME = "cookie-name"
  val COOKIE_VALUE = "cookie-value"
  val COOKIE_DOMAIN = "cookie-domain"
  val COOKIE_PATH = "cookie-path"

  val SOME_TEXT = "some-text"
  val SOME_JSON_STRING = "{}"

  case object CookieHeader extends Header {

    override def getName: String =
      COOKIE_HEADER_NAME

    override def getValue: String =
      COOKIE_HEADER_VALUE

    override def getElements: Array[HeaderElement] =
      Array()

  }

}
