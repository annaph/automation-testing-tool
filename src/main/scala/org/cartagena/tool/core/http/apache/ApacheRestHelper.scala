package org.cartagena.tool.core.http.apache

import org.apache.http.HttpEntity
import org.cartagena.tool.core.http._

trait ApacheRestHelper extends RestHelper with ApacheHttpClient with ApacheHttpOperations {

  override def startRestClient(): Unit =
    startHttpClient()

  override def restartRestClient(): Unit =
    resetHttpClient()

  override def shutdownRestClient(): Unit =
    closeHttpClient()

  override def isRestClientRunning: Boolean =
    isHttpClientUp

  override def execute[T <: HttpBody, U <: HttpBody](request: HttpRequest[T])
                                                    (implicit mf: Manifest[U]): HttpResponse[U] = {
    val f = executeFunc[T, U]
    if (f.isDefinedAt(request)) f(request) else throw new Exception("Unsupported HTTP method request!")
  }

  override def storeCookie(cookie: Cookie): Unit =
    addToCookieStore(cookie.name, cookie.value, cookie.host, cookie.path)(httpClient, httpContext)

  private def executeFunc[T <: HttpBody, U <: HttpBody](implicit mf: Manifest[U]) =
    new PartialFunction[HttpRequest[T], HttpResponse[U]] {

      override def apply(request: HttpRequest[T]): HttpResponse[U] = (request.method: @unchecked) match {
        case HttpGet =>
          executeHttpGet(request)
        case HttpPost =>
          executeHttpPost(request)
      }

      override def isDefinedAt(request: HttpRequest[T]): Boolean = request.method match {
        case UnsupportedHttpMethod => false
        case _ => true
      }
    }

  private def executeHttpGet[T <: HttpBody, U <: HttpBody](request: HttpRequest[T])
                                                          (implicit mf: Manifest[U]): HttpResponse[U] =
    createHttpResponse(
      request,
      executeGet(request.url, request.headers, request.params)(httpClient, httpContext))

  private def executeHttpPost[T <: HttpBody, U <: HttpBody](request: HttpRequest[T])
                                                           (implicit mf: Manifest[U]): HttpResponse[U] =
    createHttpResponse(
      request,
      executePost(request.url, toEntity(request.body), request.headers, request.params)(httpClient, httpContext))

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

  private def fromEntity[T <: HttpBody](entity: HttpEntity)(implicit mf: Manifest[T]): T = mf.runtimeClass match {
    case x if x == classOf[Text] =>
      fromHttpEntity[Text](entity).asInstanceOf[T]
    case x if x == classOf[JsonString] =>
      fromHttpEntity[JsonString](entity).asInstanceOf[T]
    case _ =>
      fromHttpEntity[EmptyBody.type](entity).asInstanceOf[T]
  }

  private def fromHttpEntity[T: ApacheHttpBodyConverter](entity: HttpEntity): T =
    implicitly[ApacheHttpBodyConverter[T]].fromHttpEntity(entity)

  private def toEntity[T <: HttpBody](body: T): HttpEntity = body match {
    case x@Text(_) =>
      toHttpEntity[Text](x)
    case x@JsonString(_) =>
      toHttpEntity[JsonString](x)
    case x@EmptyBody =>
      toHttpEntity[EmptyBody.type](x)
  }

  private def toHttpEntity[T: ApacheHttpBodyConverter](body: T): HttpEntity =
    implicitly[ApacheHttpBodyConverter[T]].toHttpEntity(body)

}
