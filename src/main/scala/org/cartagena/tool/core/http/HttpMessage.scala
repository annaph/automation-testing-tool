package org.cartagena.tool.core.http

import java.net.URL

sealed trait HttpMessage {

  def toPrettyString: String =
    HttpMessage.toPrettyString(this)

}

case class HttpRequest[T <: HttpBody](url: URL,
                                      method: HttpMethod,
                                      headers: List[NameValuePair] = List.empty,
                                      params: List[NameValuePair] = List.empty,
                                      body: T) extends HttpMessage

case class HttpResponse[T <: HttpBody](status: HttpStatus,
                                       reason: String,
                                       body: T,
                                       cookies: List[Cookie] = List.empty) extends HttpMessage

object HttpMessage {

  import org.cartagena.tool.core.PrettyPrintConstants.NEW_LINE

  private def toPrettyString(httpMessage: HttpMessage): String =
    httpMessage match {
      case httpRequest: HttpRequest[_] =>
        httpRequestToPrettyString(httpRequest)
      case httpResponse: HttpResponse[_] =>
        httpResponseToPrettyString(httpResponse)
    }

  private def httpRequestToPrettyString[T <: HttpBody](request: HttpRequest[T]): String = {
    val nameValuePairToStr: NameValuePair => String = {
      case NameValuePair(name, value) =>
        s"$name: $value"
    }

    val headers = request.headers.map(nameValuePairToStr)
    val params = request.params.map(nameValuePairToStr)

    val builder = StringBuilder.newBuilder

    builder ++= s"=> HTTP request:"

    builder ++= NEW_LINE
    builder ++= s"\tURL:\t\t${request.url}"

    builder ++= NEW_LINE
    builder ++= s"\tMethod:\t\t${request.method.toPrettyString}"

    builder ++= NEW_LINE
    builder ++= s"\tHeaders:"

    builder ++= NEW_LINE
    builder ++= s"${headers.map(line => s"\t\t\t$line") mkString "\n"}"

    builder ++= NEW_LINE
    builder ++= s"\tParameters:"

    builder ++= NEW_LINE
    builder ++= s"${params.map(line => s"\t\t\t$line") mkString "\n"}"

    builder ++= NEW_LINE
    builder ++= s"\tBody:"

    builder ++= NEW_LINE
    builder ++= s"${
      request.body.toPrettyString.split(NEW_LINE)
        .map(line => s"\t\t\t$line")
        .mkString(NEW_LINE)
    }"

    builder.toString()
  }

  private def httpResponseToPrettyString[T <: HttpBody](response: HttpResponse[T]): String = {
    val cookieToStr: Cookie => String = {
      case Cookie(name, value, host, path) =>
        s"$name: $value, value: $value, host: $host, path: $path"
    }

    val cookies = response.cookies.map(cookieToStr)

    val builder = StringBuilder.newBuilder

    builder ++= s"=> HTTP response:"

    builder ++= NEW_LINE
    builder ++= s"\tStatus:\t\t${response.status.toPrettyString}"

    builder ++= NEW_LINE
    builder ++= s"\tReason:\t\t${response.reason}"

    builder ++= NEW_LINE
    builder ++= s"\tCookies:"

    builder ++= NEW_LINE
    builder ++= s"${cookies.map(line => s"\t\t\t$line") mkString "\n"}"

    builder ++= NEW_LINE
    builder ++= s"\tBody:"

    builder ++= NEW_LINE
    builder ++= s"${
      response.body
        .toPrettyString
        .split(NEW_LINE)
        .map(line => s"\t\t\t$line")
        .mkString(NEW_LINE)
    }"

    builder.toString()
  }

}
