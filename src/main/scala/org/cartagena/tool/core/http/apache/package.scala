package org.cartagena.tool.core.http

import org.apache.http.HttpEntity

package object apache {

  implicit def headersToMap(headers: List[HeaderElement]): Map[String, String] =
    nameValuePairsToMap(headers)

  implicit def queryParamsToMap(queryParams: List[QueryParam]): Map[String, String] =
    nameValuePairsToMap(queryParams)

  implicit def intToHttpStatus(code: Int): HttpStatus =
    HttpStatus(code)

  implicit def toEntity[T <: HttpBody](body: T): HttpEntity =
    body match {
      case body: Text =>
        toHttpEntity[Text](body)
      case body: JsonString =>
        toHttpEntity[JsonString](body)
      case body: EmptyBody.type =>
        toHttpEntity[EmptyBody.type](body)
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

  private def toHttpEntity[T <: HttpBody : ApacheHttpBodyConverter](body: T): HttpEntity =
    implicitly[ApacheHttpBodyConverter[T]].toHttpEntity(body)

}
