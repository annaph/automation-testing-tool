package org.cartagena.tool.core

package object http {

  implicit def queryParamsToMap(queryParams: List[QueryParam]): Map[String, String] =
    queryParams.map {
      case QueryParam(name, value) =>
        name -> value
    }.toMap

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

}
