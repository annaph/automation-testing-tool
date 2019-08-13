package org.cartagena.tool.core.http.apache

import org.apache.http.client.HttpClient
import org.apache.http.protocol.HttpContext
import org.cartagena.tool.core.http._
import org.cartagena.tool.core.http.apache.ApacheHttpResponse.HttpResponseOps
import org.cartagena.tool.core.model.RestHelperComponent

trait ApacheRestHelperComponent extends RestHelperComponent {
  self: ApacheHttpClientComponent with ApacheHttpOperationsComponent =>

  private[core] val apacheHttpRestHelper: RestHelper

}

class ApacheRestHelperImpl(apacheHttpClient: ApacheHttpClient, apacheHttpOperations: ApacheHttpOperations)
  extends RestHelper {

  override def startRestClient(): Unit =
    ApacheRestHelperImpl.startRestClient(apacheHttpClient)

  override def restartRestClient(): Unit =
    ApacheRestHelperImpl.restartRestClient(apacheHttpClient)

  override def shutdownRestClient(): Unit =
    ApacheRestHelperImpl.shutdownRestClient(apacheHttpClient)

  override def isRestClientRunning: Boolean =
    ApacheRestHelperImpl.isRestClientRunning(apacheHttpClient)

  override def execute[T <: HttpBody, U <: HttpBody : Manifest](request: HttpRequest[T]): HttpResponse[U] =
    ApacheRestHelperImpl.execute(
      apacheHttpClient,
      apacheHttpOperations,
      request)(implicitly[Manifest[U]], apacheHttpClient.get, apacheHttpClient.context)

  override def storeCookie(cookie: Cookie): Unit =
    ApacheRestHelperImpl.storeCookie(apacheHttpOperations, cookie)(apacheHttpClient.get, apacheHttpClient.context)

}

object ApacheRestHelperImpl {

  private val COOKIE_PATH = "/"

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
      apacheHttpOperations executePost(request.url, request.body, request.headers, request.params))

  private def createHttpResponse[T <: HttpBody, U <: HttpBody](request: HttpRequest[T],
                                                               apacheResponse: ApacheHttpResponse)
                                                              (implicit mf: Manifest[U]): HttpResponse[U] = {
    HttpResponse(
      status = apacheResponse.nativeResponse.statusCode,
      reason = apacheResponse.nativeResponse.reasonPhrase,
      body = apacheResponse.nativeResponse.httpBody,
      cookies = toCookies(
        apacheResponse.nativeResponse.cookieHeaderElements, request.url.getHost, COOKIE_PATH))
  }

}
