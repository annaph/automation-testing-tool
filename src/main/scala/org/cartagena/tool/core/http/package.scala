package org.cartagena.tool.core

import com.fasterxml.jackson.databind.ObjectMapper

package object http {

  val NEW_LINE: String = System getProperty "line.separator"

  val mapper: ObjectMapper = new ObjectMapper()

  implicit def headersToMap(headers: List[HeaderElement]): Map[String, String] =
    nameValuePairsToMap(headers)

  implicit def queryParamsToMap(queryParams: List[QueryParam]): Map[String, String] =
    nameValuePairsToMap(queryParams)

  implicit def intToHttpStatus(statusCode: Int): HttpStatus = statusCode match {
    case 200 =>
      HttpStatusOK
    case 201 =>
      HttpStatusCreated
    case 500 =>
      HttpStatusServerError
    case _ =>
      UnsupportedHttpStatus
  }

  def prettyString[T <: HttpBody](request: HttpRequest[T]): String = {
    val headers = request.headers.map {
      case HeaderElement(name, value) =>
        s"$name: $value"
    }

    val params = request.params.map {
      case QueryParam(name, value) =>
        s"$name = $value"
    }

    val builder = StringBuilder.newBuilder

    builder ++= NEW_LINE
    builder ++= s"=> HTTP request:"

    builder ++= NEW_LINE
    builder ++= s"\tURL:    \t${request.url}"

    builder ++= NEW_LINE
    builder ++= s"\tMethod: \t${request.method}"

    builder ++= NEW_LINE
    builder ++= s"\tHeaders:"

    builder ++= NEW_LINE
    builder ++= s" \t\t${headers mkString "\n \t\t"}"

    builder ++= NEW_LINE
    builder ++= s"\tParameters:"

    builder ++= NEW_LINE
    builder ++= s" \t\t${params mkString "\n \t\t"}"

    builder ++= NEW_LINE
    builder ++= s"\tBody:   \t${request.body}"

    builder ++= NEW_LINE

    builder.toString()
  }

  def prettyString[T <: HttpBody](response: HttpResponse[T]): String = {
    val cookies = response.cookies.map {
      case Cookie(name, value, host, path) =>
        s"$name: $value, value: $value, host: $host, path: $path"
    }


    val builder = StringBuilder.newBuilder

    builder ++= NEW_LINE
    builder ++= s"=> HTTP response:"

    builder ++= NEW_LINE
    builder ++= s"\tStatus:    \t${response.status}"

    builder ++= NEW_LINE
    builder ++= s"\tReason:    \t${response.reason}"

    builder ++= NEW_LINE
    builder ++= s"\tCookies:"

    builder ++= NEW_LINE
    builder ++= s" \t\t${cookies mkString "\n \t\t"}"

    builder ++= NEW_LINE
    builder ++= s"\tBody:"

    builder ++= NEW_LINE
    builder ++= s"${
      response.body
        .map(_.toString)
        .map(_.split(NEW_LINE)
          .map(line => s"\t\t\t$line")
          .mkString(NEW_LINE))
        .getOrElse("\t<none>")
    }"

    builder ++= NEW_LINE

    builder.toString()
  }

  def prettyString(jsonString: JsonString): String = {
    val json = mapper readValue(jsonString.str, classOf[Object])
    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json)
  }

  private def nameValuePairsToMap[T <: NameValuePair](pairs: List[T]): Map[String, String] =
    pairs.map { pair =>
      pair.name -> pair.value
    }.toMap

}
