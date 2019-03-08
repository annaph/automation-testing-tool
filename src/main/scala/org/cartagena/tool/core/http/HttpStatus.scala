package org.cartagena.tool.core.http

sealed trait HttpStatus

case object HttpStatusOK extends HttpStatus

case object HttpStatusCreated extends HttpStatus

case object HttpStatusServerError extends HttpStatus

case object UnsupportedHttpStatus extends HttpStatus

object HttpStatus {

  def apply(code: Int): HttpStatus = code match {
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
