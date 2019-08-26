package org.cartagena.tool.core.http

sealed trait HttpMethod {

  def toPrettyString: String =
    HttpMethod.toPrettyString(this)

}

case object Get extends HttpMethod

case object Post extends HttpMethod

case object Delete extends HttpMethod

object HttpMethod {

  val HTTP_METHOD_GET = "GET"

  val HTTP_METHOD_POST = "POST"

  val HTTP_METHOD_DELETE = "DELETE"

  private[http] def toPrettyString(httpMethod: HttpMethod): String =
    httpMethod match {
      case Get =>
        HTTP_METHOD_GET
      case Post =>
        HTTP_METHOD_POST
      case Delete =>
        HTTP_METHOD_DELETE
    }

}
