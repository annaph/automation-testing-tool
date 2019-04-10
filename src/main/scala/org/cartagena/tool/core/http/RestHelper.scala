package org.cartagena.tool.core.http

import org.cartagena.tool.core.model.{Helper, HttpNativeClient, HttpOperations}

trait RestHelper extends Helper {
  self: HttpNativeClient with HttpOperations =>

  def startRestClient(): Unit

  def restartRestClient(): Unit

  def shutdownRestClient(): Unit

  def isRestClientRunning: Boolean

  def execute[T <: HttpBody, U <: HttpBody](request: HttpRequest[T])(implicit mf: Manifest[U]): HttpResponse[U]

  def storeCookie(cookie: Cookie): Unit

}
