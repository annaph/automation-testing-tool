package org.cartagena.tool.core.http.apache

import org.apache.http.HttpEntity
import org.apache.http.client.HttpClient
import org.apache.http.protocol.HttpContext
import org.cartagena.tool.core.http._

trait ApacheRestHelper extends RestHelper with ApacheHttpClient with ApacheHttpOperations {

  implicit private val restClient: HttpClient =
    this.httpClient

  implicit private val restContext: HttpContext =
    this.httpContext

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

  private def executeFunc[T <: HttpBody, U <: HttpBody](implicit mf: Manifest[U]) =
    new PartialFunction[HttpRequest[T], HttpResponse[U]] {

      override def apply(request: HttpRequest[T]): HttpResponse[U] = (request.method: @unchecked) match {
        case HttpGet =>
          ???
        case HttpPost =>
          executeHttpPost(request)
      }

      override def isDefinedAt(request: HttpRequest[T]): Boolean = request.method match {
        case UnsupportedHttpMethod => false
        case _ => true
      }
    }

  private def executeHttpPost[T <: HttpBody, U <: HttpBody](request: HttpRequest[T])
                                                           (implicit mf: Manifest[U]): HttpResponse[U] = {
    val postResponse = executePost(
      request.url,
      toEntity(request.body),
      request.headers,
      request.params)

    val cookieHeaderElements = Option {
      postResponse.getFirstHeader("Set-Cookie")
    }.map(_.getElements.map { header =>
      Cookie(header.getName, header.getValue, request.url.getHost, "/")
    })

    HttpResponse(
      status = postResponse.getStatusLine.getStatusCode,
      reason = postResponse.getStatusLine.getReasonPhrase,
      body = Option(postResponse.getEntity).map(fromEntity(_)),
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

  override def storeCookie(cookie: Cookie): Unit =
    addToCookieStore(cookie.name, cookie.value, cookie.host, cookie.path)

}
