package org.cartagena.tool.core.http

sealed trait HttpStatus {

  def toPrettyString: String =
    HttpStatus.toPrettyString(this)

}

case object OK extends HttpStatus

case object Created extends HttpStatus

case object Accepted extends HttpStatus

case object NoContent extends HttpStatus

case object InternalServerError extends HttpStatus

case object UnsupportedStatus extends HttpStatus

object HttpStatus {

  val HTTP_STATUS_OK_CODE = 200

  val HTTP_STATUS_CREATED_CODE = 201

  val HTTP_STATUS_ACCEPTED_CODE = 202

  val HTTP_STATUS_NO_CONTENT_CODE = 204

  val HTTP_STATUS_INTERNAL_SERVER_ERROR_CODE = 500

  val HTTP_STATUS_OK = "OK"

  val HTTP_STATUS_CREATED = "Created"

  val HTTP_STATUS_ACCEPTED = "Accepted"

  val HTTP_STATUS_NO_CONTENT = "No Content"

  val HTTP_STATUS_INTERNAL_SERVER_ERROR = "Internal Server Error"

  val HTTP_UNSUPPORTED_STATUS = "Unsupported"

  def apply(status: Int): HttpStatus =
    status match {
      case HTTP_STATUS_OK_CODE =>
        OK
      case HTTP_STATUS_CREATED_CODE =>
        Created
      case HTTP_STATUS_ACCEPTED_CODE =>
        Accepted
      case HTTP_STATUS_NO_CONTENT_CODE =>
        NoContent
      case HTTP_STATUS_INTERNAL_SERVER_ERROR_CODE =>
        InternalServerError
      case _ =>
        UnsupportedStatus
    }

  private def toPrettyString(httpStatus: HttpStatus): String =
    httpStatus match {
      case OK =>
        HTTP_STATUS_OK
      case Created =>
        HTTP_STATUS_CREATED
      case Accepted =>
        HTTP_STATUS_ACCEPTED
      case NoContent =>
        HTTP_STATUS_NO_CONTENT
      case InternalServerError =>
        HTTP_STATUS_INTERNAL_SERVER_ERROR
      case UnsupportedStatus =>
        HTTP_UNSUPPORTED_STATUS
    }

}
