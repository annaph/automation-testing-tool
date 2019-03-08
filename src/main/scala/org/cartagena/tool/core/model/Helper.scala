package org.cartagena.tool.core.model

import org.cartagena.tool.core.http.{HttpBody, HttpRequest, HttpResponse}

sealed trait Client

trait RestClient extends Client

trait JdbcClient extends Client

sealed trait Operations

trait RestOperations extends Operations

trait JdbcOperations extends Operations

sealed trait Helper {
  self: Client with Operations =>
}

trait RestHelper extends Helper {
  self: RestClient with RestOperations =>

  def startRestClient(): Unit

  def restartRestClient(): Unit

  def shutdownRestClient(): Unit

  def isRestClientRunning: Boolean

  def execute[T <: HttpBody, U <: HttpBody](request: HttpRequest[T]): HttpResponse[U]

}

trait JdbcHelper extends {
  self: JdbcClient with JdbcOperations =>
}
