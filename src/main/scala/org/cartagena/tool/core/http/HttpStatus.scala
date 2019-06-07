package org.cartagena.tool.core.http

sealed trait HttpStatus {

  def toPrettyString: String =
    HttpStatus.toPrettyString(this)

}

case object OK extends HttpStatus

case object Created extends HttpStatus

case object ServerError extends HttpStatus

case object UnsupportedStatus extends HttpStatus

object HttpStatus {

  val HTTP_STATUS_OK_CODE = 200

  val HTTP_STATUS_CREATED_CODE = 201

  val HTTP_STATUS_SERVER_ERROR_CODE = 500

  val HTTP_STATUS_OK = "OK"

  val HTTP_STATUS_CREATED = "Created"

  val HTTP_STATUS_SERVER_ERROR = "Server Error"

  val HTTP_UNSUPPORTED_STATUS = "Unsupported"

  def apply(status: Int): HttpStatus =
    status match {
      case HTTP_STATUS_OK_CODE =>
        OK
      case HTTP_STATUS_CREATED_CODE =>
        Created
      case HTTP_STATUS_SERVER_ERROR_CODE =>
        ServerError
      case _ =>
        UnsupportedStatus
    }

  private[http] def toPrettyString(httpStatus: HttpStatus): String =
    httpStatus match {
      case OK =>
        HTTP_STATUS_OK
      case Created =>
        HTTP_STATUS_CREATED
      case ServerError =>
        HTTP_STATUS_SERVER_ERROR
      case UnsupportedStatus =>
        HTTP_UNSUPPORTED_STATUS
    }

}
