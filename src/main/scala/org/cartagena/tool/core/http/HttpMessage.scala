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

class HttpRequest[T <: HttpBody](val url: URL,
                                 val method: HttpMethod,
                                 val headers: List[HeaderElement] = List.empty,
                                 val params: List[QueryParam] = List.empty,
                                 val body: T) extends HttpMessage {

  override def toString: String =
    HttpRequest.prettyString(this)

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

  def prettyString[T <: HttpBody](request: HttpRequest[T]): String = {
    val builder = StringBuilder.newBuilder

    val newLine = System getProperty "line.separator"

    builder ++= newLine
    builder ++= s"HTTP request info:"

    builder ++= newLine
    builder ++= s"\tURL: \t\t\t${request.url.toString}"

    builder.toString()
  }

}

case class HttpResponse[T <: HttpBody](status: HttpStatus,
                                       reason: String,
                                       body: Option[T] = None,
                                       cookies: List[Cookie] = List.empty)
