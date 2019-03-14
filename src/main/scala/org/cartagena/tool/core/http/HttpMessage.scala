package org.cartagena.tool.core.http

import java.net.URL

sealed trait HttpMethod

case object HttpGet extends HttpMethod

case object HttpPost extends HttpMethod

case object UnsupportedHttpMethod extends HttpMethod

trait NameValuePair {

  def name: String

  def value: String

}

case class HeaderElement(name: String, value: String) extends NameValuePair

case class QueryParam(name: String, value: String) extends NameValuePair

case class Cookie(name: String, value: String, host: String, path: String) extends NameValuePair

sealed trait HttpBody extends Any

case class Text(str: String) extends AnyVal with HttpBody

case class JsonString(str: String) extends AnyVal with HttpBody

case object EmptyBody extends HttpBody

sealed trait HttpStatus

case object HttpStatusOK extends HttpStatus

case object HttpStatusCreated extends HttpStatus

case object HttpStatusServerError extends HttpStatus

case object UnsupportedHttpStatus extends HttpStatus

trait HttpMessage

case class HttpRequest[T <: HttpBody](url: URL,
                                      method: HttpMethod,
                                      headers: List[HeaderElement] = List.empty,
                                      params: List[QueryParam] = List.empty,
                                      body: Option[T] = None) extends HttpMessage
