package org.cartagena.tool.core.http.apache

import java.net.URI

import org.apache.http.client.methods.{HttpGet, HttpPost, HttpRequestBase}
import org.apache.http.{Header, HttpEntity, HttpResponse, HeaderElement => ApacheHeaderElement}
import org.cartagena.tool.core.http.{EmptyBody, HeaderElement, HttpBody, JsonString, Text}

sealed trait ApacheHttpRequest extends HttpRequestBase {

  def id: Long

  def getURI: URI

  def getAllHeaders: Array[Header]

  def getEntity: HttpEntity

  def addHeader(name: String, value: String): Unit

  def setEntity(entity: HttpEntity): Unit

}

class ApacheHttpGet(val id: Long, val uri: URI) extends HttpGet(uri) with ApacheHttpRequest {

  override def getEntity: HttpEntity =
    throw new UnsupportedOperationException("HTTP GET request has no entity!")

  override def setEntity(entity: HttpEntity): Unit =
    throw new UnsupportedOperationException("Cannot set entity to HTTP GET request!")

}

class ApacheHttpPost(val id: Long, val uri: URI) extends HttpPost with ApacheHttpRequest {

  override def getURI: URI =
    uri

}

class ApacheHttpDelete(val id: Long, val uri: URI) extends HttpPost with ApacheHttpRequest {

  override def getMethod: String =
    "DELETE"

  override def getURI: URI =
    uri

}

class ApacheHttpResponse(val id: Long, val nativeResponse: HttpResponse) {

  override def equals(that: Any): Boolean = that match {
    case that: ApacheHttpResponse =>
      this.hashCode == that.hashCode
    case _ =>
      false
  }

  override def hashCode(): Int = {
    val prime = 31
    var result = 1

    result = prime * result + id.hashCode()
    result = prime * result + nativeResponse.hashCode()

    result
  }

}

object ApacheHttpResponse {

  private[apache] val COOKIE_HEADER_NAME = "Set-Cookie"

  def apply(id: Long, response: HttpResponse) =
    new ApacheHttpResponse(id, response)

  implicit class HttpResponseOps(httpResponse: HttpResponse) {

    def statusCode: Int =
      httpResponse.getStatusLine.getStatusCode

    def reasonPhrase: String =
      httpResponse.getStatusLine.getReasonPhrase

    def cookieHeaderElements: List[HeaderElement] =
      Option(httpResponse.getFirstHeader(COOKIE_HEADER_NAME))
        .map(_.getElements)
        .map(toHeaderElements)
        .getOrElse(List.empty)

    def httpBody[T <: HttpBody](implicit mf: Manifest[T]): T =
      mf.runtimeClass match {
        case x if x == classOf[Text] =>
          httpBody[Text](httpResponse.getEntity).asInstanceOf[T]
        case x if x == classOf[JsonString] =>
          httpBody[JsonString](httpResponse.getEntity).asInstanceOf[T]
        case _ =>
          httpBody[EmptyBody.type](httpResponse.getEntity).asInstanceOf[T]
      }

    private def toHeaderElements(apacheHeaderElements: Array[ApacheHeaderElement]): List[HeaderElement] =
      apacheHeaderElements.map { header =>
        HeaderElement(header.getName, header.getValue)
      }.toList

    private def httpBody[T <: HttpBody : ApacheHttpBodyConverter](entity: HttpEntity): T =
      implicitly[ApacheHttpBodyConverter[T]] fromHttpEntity entity

  }

}
