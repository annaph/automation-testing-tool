package org.cartagena.tool.core.http

import java.net.URL

sealed trait HttpMethod

case object HttpGet extends HttpMethod {

  override def toString: String =
    "GET"

}

case object HttpPost extends HttpMethod {

  override def toString: String =
    "POST"

}

case object UnsupportedHttpMethod extends HttpMethod

trait NameValuePair {

  def name: String

  def value: String

}

case class HeaderElement(name: String, value: String) extends NameValuePair

case class QueryParam(name: String, value: String) extends NameValuePair

case class Cookie(name: String, value: String, host: String, path: String) extends NameValuePair

sealed trait HttpBody extends Any

case class Text(str: String) extends AnyVal with HttpBody {

  override def toString: String =
    str

}

case class JsonString(str: String) extends AnyVal with HttpBody {

  override def toString: String =
    prettyString(this)

}

case object EmptyBody extends HttpBody {

  override def toString: String =
    "<empty>"

}

sealed trait HttpStatus

case object HttpStatusOK extends HttpStatus {

  override def toString: String =
    "OK"

}

case object HttpStatusCreated extends HttpStatus {

  override def toString: String =
    "Created"

}

case object HttpStatusServerError extends HttpStatus {

  override def toString: String =
    "Server error"

}

case object UnsupportedHttpStatus extends HttpStatus

trait HttpMessage

class HttpRequest[T <: HttpBody](val url: URL,
                                 val method: HttpMethod,
                                 val headers: List[HeaderElement] = List.empty,
                                 val params: List[QueryParam] = List.empty,
                                 val body: T) extends HttpMessage {

  override def toString: String =
    prettyString(this)

}

object HttpRequest {

  def apply[T <: HttpBody](url: URL,
                           method: HttpMethod,
                           headers: List[(String, String)] = List.empty,
                           params: List[(String, String)] = List.empty,
                           body: T): HttpRequest[T] =
    new HttpRequest(
      url,
      method,
      headers.map(header => HeaderElement(header._1, header._2)),
      params.map(param => QueryParam(param._1, param._2)),
      body)

}

case class HttpResponse[T <: HttpBody](status: HttpStatus,
                                       reason: String,
                                       body: Option[T] = None,
                                       cookies: List[Cookie] = List.empty) {

  override def toString: String =
    prettyString(this)

}
