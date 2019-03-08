package org.cartagena.tool.core.http.apache

import org.apache.http.HttpEntity
import org.cartagena.tool.core.http.{HttpBody, HttpRequest, HttpResponse}
import org.cartagena.tool.core.model.RestHelper
import org.cartagena.tool.core.http.Text
import org.cartagena.tool.core.http.JsonString
import org.cartagena.tool.core.http.EmptyBody

trait ApacheRestHelper extends RestHelper with ApacheRestClient with ApacheRestOperations {

  override def startRestClient(): Unit = startHttpClient()

  override def restartRestClient(): Unit = resetHttpClient()

  override def shutdownRestClient(): Unit = closeHttpClient()

  override def isRestClientRunning: Boolean = isHttpClientUp

  override def execute[T <: HttpBody, U <: HttpBody](request: HttpRequest[T]): HttpResponse[U] = {
    ???
  }

  private def httpEntity[T <: HttpBody](body: T): HttpEntity = body match {
    case x@ Text(str) =>
      toHttpEntity[Text](x)
    case JsonString(_) => ???
    case EmptyBody => ???
  }

  private def toHttpEntity[T: ApacheBodyConverter](body: T): HttpEntity =
    implicitly[ApacheBodyConverter[T]].toHttpEntity(body)

}
