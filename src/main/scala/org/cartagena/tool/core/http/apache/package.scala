package org.cartagena.tool.core.http

package object apache {

  implicit def headersToMap(headers: List[HeaderElement]): Map[String, String] =
    nameValuePairsToMap(headers)

  implicit def queryParamsToMap(queryParams: List[QueryParam]): Map[String, String] =
    nameValuePairsToMap(queryParams)

  implicit def intToHttpStatus(code: Int): HttpStatus =
    HttpStatus(code)

  private def nameValuePairsToMap[T <: NameValuePair](pairs: List[T]): Map[String, String] =
    pairs.map { pair =>
      pair.name -> pair.value
    }.toMap

}
