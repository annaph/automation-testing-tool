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
      request, apacheHttpOperations)(implicitly[Manifest[U]], apacheHttpClient.get, apacheHttpClient.context)

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

  private def execute[T <: HttpBody, U <: HttpBody](request: HttpRequest[T],
                                                    apacheHttpOperations: ApacheHttpOperations)
                                                   (implicit mf: Manifest[U],
                                                    client: HttpClient,
                                                    context: HttpContext): HttpResponse[U] =
    request.method match {
      case Get =>
        executeHttpGet(request, apacheHttpOperations)
      case Post =>
        executeHttpPost(request, apacheHttpOperations)
      case Put =>
        executeHttpPut(request, apacheHttpOperations)
      case Delete =>
        executeHttpDelete(request, apacheHttpOperations)
    }

  private def storeCookie(apacheHttpOperations: ApacheHttpOperations, cookie: Cookie)
                         (implicit client: HttpClient, context: HttpContext): Unit =
    apacheHttpOperations addToCookieStore(cookie.name, cookie.value, cookie.host, cookie.path)

  private def executeHttpGet[T <: HttpBody, U <: HttpBody](request: HttpRequest[T],
                                                           apacheHttpOperations: ApacheHttpOperations)
                                                          (implicit mf: Manifest[U],
                                                           client: HttpClient,
                                                           context: HttpContext): HttpResponse[U] =
    createHttpResponse(
      request.url.getHost,
      apacheHttpOperations executeGet(request.url, request.headers, request.params))

  private def executeHttpPost[T <: HttpBody, U <: HttpBody](request: HttpRequest[T],
                                                            apacheHttpOperations: ApacheHttpOperations)
                                                           (implicit mf: Manifest[U],
                                                            client: HttpClient,
                                                            context: HttpContext): HttpResponse[U] =
    createHttpResponse(
      request.url.getHost,
      apacheHttpOperations executePost(request.url, request.headers, request.params, request.body))

  private def executeHttpPut[T <: HttpBody, U <: HttpBody](request: HttpRequest[T],
                                                           apacheHttpOperations: ApacheHttpOperations)
                                                          (implicit mf: Manifest[U],
                                                           client: HttpClient,
                                                           context: HttpContext): HttpResponse[U] =
    createHttpResponse(
      request.url.getHost,
      apacheHttpOperations executePut(request.url, request.headers, request.params, request.body))

  private def executeHttpDelete[T <: HttpBody, U <: HttpBody](request: HttpRequest[T],
                                                              apacheHttpOperations: ApacheHttpOperations)
                                                             (implicit mf: Manifest[U],
                                                              client: HttpClient,
                                                              context: HttpContext): HttpResponse[U] = {
    createHttpResponse(
      request.url.getHost,
      apacheHttpOperations executeDelete(request.url, request.headers, request.params, request.body))
  }

  private def createHttpResponse[T <: HttpBody, U <: HttpBody](host: String, apacheResponse: => ApacheHttpResponse)
                                                              (implicit mf: Manifest[U]): HttpResponse[U] = {
    lazy val response = apacheResponse

    HttpResponse(
      status = response.nativeResponse.statusCode,
      reason = response.nativeResponse.reasonPhrase,
      body = response.nativeResponse.httpBody,
      cookies = toCookies(
        response.nativeResponse.cookieHeaderElements, host, COOKIE_PATH))
  }

}
