package org.cartagena.tool.core.http

trait RestHelper {

  def startRestClient(): Unit

  def restartRestClient(): Unit

  def shutdownRestClient(): Unit

  def isRestClientRunning: Boolean

  def execute[T <: HttpBody, U <: HttpBody](request: HttpRequest[T])(implicit mf: Manifest[U]): HttpResponse[U]

  def storeCookie(cookie: Cookie): Unit

}
