package org.cartagena.tool.core.http

import java.net.URL

sealed trait HttpMessage {

  def toPrettyString: String =
    HttpMessage.toPrettyString(this)

}

class HttpRequest[T <: HttpBody](val url: URL,
                                 val method: HttpMethod,
                                 val headers: List[HeaderElement] = List.empty,
                                 val params: List[QueryParam] = List.empty,
                                 val body: T) extends HttpMessage

case class HttpResponse[T <: HttpBody](status: HttpStatus,
                                       reason: String,
                                       body: Option[T] = None,
                                       cookies: List[Cookie] = List.empty) extends HttpMessage

object HttpMessage {

  private val _NEW_LINE: String = System getProperty "line.separator"

  private[http] def toPrettyString(httpMessage: HttpMessage): String =
    httpMessage match {
      case httpRequest: HttpRequest[_] =>
        httpRequestToPrettyString(httpRequest)
      case httpResponse: HttpResponse[_] =>
        httpResponseToPrettyString(httpResponse)
    }

  private def httpRequestToPrettyString[T <: HttpBody](request: HttpRequest[T]): String = {
    val headers = request.headers.map {
      case HeaderElement(name, value) =>
        s"$name: $value"
    }

    val params = request.params.map {
      case QueryParam(name, value) =>
        s"$name = $value"
    }

    val builder = StringBuilder.newBuilder

    builder ++= s"=> HTTP request:"

    builder ++= _NEW_LINE
    builder ++= s"\tURL:\t\t${request.url}"

    builder ++= _NEW_LINE
    builder ++= s"\tMethod:\t\t${request.method.toPrettyString}"

    builder ++= _NEW_LINE
    builder ++= s"\tHeaders:"

    builder ++= _NEW_LINE
    builder ++= s"${headers.map(line => s"\t\t\t$line") mkString "\n"}"

    builder ++= _NEW_LINE
    builder ++= s"\tParameters:"

    builder ++= _NEW_LINE
    builder ++= s"${params.map(line => s"\t\t\t$line") mkString "\n"}"

    builder ++= _NEW_LINE
    builder ++= s"\tBody:"

    builder ++= _NEW_LINE
    builder ++= s"${
      request.body.toPrettyString.split(_NEW_LINE)
        .map(line => s"\t\t\t$line")
        .mkString(_NEW_LINE)
    }"

    builder.toString()
  }

  private def httpResponseToPrettyString[T <: HttpBody](response: HttpResponse[T]): String = {
    val cookies = response.cookies.map {
      case Cookie(name, value, host, path) =>
        s"$name: $value, value: $value, host: $host, path: $path"
    }

    val builder = StringBuilder.newBuilder

    builder ++= s"=> HTTP response:"

    builder ++= _NEW_LINE
    builder ++= s"\tStatus:\t\t${response.status.toPrettyString}"

    builder ++= _NEW_LINE
    builder ++= s"\tReason:\t\t${response.reason}"

    builder ++= _NEW_LINE
    builder ++= s"\tCookies:"

    builder ++= _NEW_LINE
    builder ++= s"${cookies.map(line => s"\t\t\t$line") mkString "\n"}"

    builder ++= _NEW_LINE
    builder ++= s"\tBody:"

    builder ++= _NEW_LINE
    builder ++= s"${
      response.body
        .map(_.toPrettyString)
        .map(_.split(_NEW_LINE)
          .map(line => s"\t\t\t$line")
          .mkString(_NEW_LINE))
        .getOrElse("\t<none>")
    }"

    builder.toString()
  }

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
