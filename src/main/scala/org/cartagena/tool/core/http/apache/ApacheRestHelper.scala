package org.cartagena.tool.core.http.apache

import org.apache.http.HttpEntity
import org.apache.http.client.HttpClient
import org.apache.http.protocol.HttpContext
import org.cartagena.tool.core.http._
import org.cartagena.tool.core.model.RestHelperComponent

trait ApacheRestHelperComponent extends RestHelperComponent {
  self: ApacheHttpClientComponent with ApacheHttpOperationsComponent =>

  private[core] val apacheHttpRestHelper: RestHelper

}

class ApacheRestHelper(apacheHttpClient: ApacheHttpClient, apacheHttpOperations: ApacheHttpOperations)
  extends RestHelper {

  override def startRestClient(): Unit =
    ApacheRestHelper.startRestClient(apacheHttpClient)

  override def restartRestClient(): Unit =
    ApacheRestHelper.restartRestClient(apacheHttpClient)

  override def shutdownRestClient(): Unit =
    ApacheRestHelper.shutdownRestClient(apacheHttpClient)

  override def isRestClientRunning: Boolean =
    ApacheRestHelper.isRestClientRunning(apacheHttpClient)

  override def execute[T <: HttpBody, U <: HttpBody](request: HttpRequest[T])
                                                    (implicit mf: Manifest[U]): HttpResponse[U] =
    ApacheRestHelper
      .execute(apacheHttpClient, apacheHttpOperations, request)(mf, apacheHttpClient.get, apacheHttpClient.context)

  override def storeCookie(cookie: Cookie): Unit =
    ApacheRestHelper.storeCookie(apacheHttpOperations, cookie)(apacheHttpClient.get, apacheHttpClient.context)

}

object ApacheRestHelper {

  private def startRestClient(apacheHttpClient: ApacheHttpClient): Unit =
    apacheHttpClient.start()

  private def restartRestClient(apacheHttpClient: ApacheHttpClient): Unit =
    apacheHttpClient.reset()

  private def shutdownRestClient(apacheHttpClient: ApacheHttpClient): Unit =
    apacheHttpClient.close()

  private def isRestClientRunning(apacheHttpClient: ApacheHttpClient): Boolean =
    apacheHttpClient.isUp

  private def execute[T <: HttpBody, U <: HttpBody](apacheHttpClient: ApacheHttpClient,
                                                    apacheHttpOperations: ApacheHttpOperations,
                                                    request: HttpRequest[T])
                                                   (implicit mf: Manifest[U],
                                                    client: HttpClient,
                                                    context: HttpContext): HttpResponse[U] =
    request.method match {
      case Get =>
        executeHttpGet(apacheHttpOperations, request)
      case Post =>
        executeHttpPost(apacheHttpClient, apacheHttpOperations, request)
    }

  private def storeCookie(apacheHttpOperations: ApacheHttpOperations, cookie: Cookie)
                         (implicit client: HttpClient, context: HttpContext): Unit =
    apacheHttpOperations addToCookieStore(cookie.name, cookie.value, cookie.host, cookie.path)

  private def executeHttpGet[T <: HttpBody, U <: HttpBody](apacheHttpOperations: ApacheHttpOperations,
                                                           request: HttpRequest[T])
                                                          (implicit mf: Manifest[U],
                                                           client: HttpClient,
                                                           context: HttpContext): HttpResponse[U] =
    createHttpResponse(
      request,
      apacheHttpOperations executeGet(request.url, request.headers, request.params))

  private def executeHttpPost[T <: HttpBody, U <: HttpBody](apacheHttpClient: ApacheHttpClient,
                                                            apacheHttpOperations: ApacheHttpOperations,
                                                            request: HttpRequest[T])
                                                           (implicit mf: Manifest[U],
                                                            client: HttpClient,
                                                            context: HttpContext): HttpResponse[U] =
    createHttpResponse(
      request,
      apacheHttpOperations executePost(request.url, toEntity(request.body), request.headers, request.params))

  private def createHttpResponse[T <: HttpBody, U <: HttpBody](request: HttpRequest[T],
                                                               apacheResponse: ApacheHttpResponse)
                                                              (implicit mf: Manifest[U]): HttpResponse[U] = {
    val cookieHeaderElements = Option {
      apacheResponse.get.getFirstHeader("Set-Cookie")
    }.map(_.getElements.map { header =>
      Cookie(header.getName, header.getValue, request.url.getHost, "/")
    })

    HttpResponse(
      status = apacheResponse.get.getStatusLine.getStatusCode,
      reason = apacheResponse.get.getStatusLine.getReasonPhrase,
      body = Option(apacheResponse.get.getEntity).map(fromEntity(_)),
      cookies = cookieHeaderElements.map(_.toList).getOrElse(List.empty))
  }

  private def fromEntity[T <: HttpBody](entity: HttpEntity)(implicit mf: Manifest[T]): T =
    mf.runtimeClass match {
      case x if x == classOf[Text] =>
        fromHttpEntity[Text](entity).asInstanceOf[T]
      case x if x == classOf[JsonString] =>
        fromHttpEntity[JsonString](entity).asInstanceOf[T]
      case _ =>
        fromHttpEntity[EmptyBody.type](entity).asInstanceOf[T]
    }

  private def toEntity[T <: HttpBody](body: T): HttpEntity =
    body match {
      case x@Text(_) =>
        toHttpEntity[Text](x)
      case x@JsonString(_) =>
        toHttpEntity[JsonString](x)
      case x@EmptyBody =>
        toHttpEntity[EmptyBody.type](x)
    }

  private def fromHttpEntity[T <: HttpBody : ApacheHttpBodyConverter](entity: HttpEntity): T =
    implicitly[ApacheHttpBodyConverter[T]].fromHttpEntity(entity)

  private def toHttpEntity[T <: HttpBody : ApacheHttpBodyConverter](body: T): HttpEntity =
    implicitly[ApacheHttpBodyConverter[T]].toHttpEntity(body)

}
