package org.cartagena.tool.core.http

import java.io.{BufferedReader, InputStream, InputStreamReader}
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

import org.apache.http.HttpEntity

package object apache {

  implicit def headersToMap(headerElements: List[HeaderElement]): Map[String, String] =
    nameValuePairsToMap(headerElements)

  implicit def queryParamsToMap(queryParams: List[QueryParam]): Map[String, String] =
    nameValuePairsToMap(queryParams)

  implicit def intToHttpStatus(code: Int): HttpStatus =
    HttpStatus(code)

  implicit def inputStreamToString(in: InputStream): String = {
    val reader = new BufferedReader(
      new InputStreamReader(in, StandardCharsets.UTF_8.name()))

    reader.lines().collect(Collectors.joining(System.lineSeparator()))
  }

  implicit def toHttpEntity[T <: HttpBody](body: T): HttpEntity =
    body match {
      case x: Text =>
        toEntity[Text](x)
      case x: JsonString =>
        toEntity[JsonString](x)
      case x@Empty =>
        toEntity[Empty.type](x)
    }

  implicit def toHttpEntityOption[T <: HttpBody](body: T): Option[HttpEntity] =
    body match {
      case Empty =>
        None
      case x =>
        Some(x)
    }

  def toCookies(headerElements: List[HeaderElement], host: String, path: String): List[Cookie] =
    headerElements.map {
      case HeaderElement(name, value) =>
        Cookie(name, value, host, path)
    }

  private def nameValuePairsToMap[T <: NameValuePair](pairs: List[T]): Map[String, String] =
    pairs.map { pair =>
      pair.name -> pair.value
    }.toMap

  private def toEntity[T <: HttpBody : ApacheHttpBodyConverter](body: T): HttpEntity =
    implicitly[ApacheHttpBodyConverter[T]].toHttpEntity(body)

}
