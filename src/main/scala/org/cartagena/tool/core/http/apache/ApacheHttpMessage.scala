package org.cartagena.tool.core.http.apache

import java.net.URI
import java.util.Objects

import org.apache.http.client.methods.{HttpGet, HttpPost, HttpPut, HttpRequestBase}
import org.apache.http.{Header, HttpEntity, HttpResponse, HeaderElement => ApacheHeaderElement}
import org.cartagena.tool.core.http._

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
    throw new UnsupportedOperationException

  override def setEntity(entity: HttpEntity): Unit =
    throw new UnsupportedOperationException

}

class ApacheHttpPost(val id: Long, val uri: URI) extends HttpPost with ApacheHttpRequest {

  override def getURI: URI =
    uri

}

class ApacheHttpPut(val id: Long, val uri: URI) extends HttpPut with ApacheHttpRequest {

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

  override def hashCode(): Int =
    Objects.hash(id.asInstanceOf[Object], nativeResponse.asInstanceOf[Object])

  override def equals(that: Any): Boolean = that match {
    case _: ApacheHttpResponse =>
      this.hashCode == that.hashCode
    case _ =>
      false
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

    def cookieHeaderElements: List[NameValuePair] =
      Option(httpResponse.getFirstHeader(COOKIE_HEADER_NAME))
        .map(_.getElements)
        .map(toNameValuePairs)
        .getOrElse(List.empty)

    def httpBody[T <: HttpBody](implicit mf: Manifest[T]): T =
      mf.runtimeClass match {
        case x if x == classOf[Text] =>
          httpBody[Text](httpResponse.getEntity).asInstanceOf[T]
        case x if x == classOf[JsonString] =>
          httpBody[JsonString](httpResponse.getEntity).asInstanceOf[T]
        case _ =>
          httpBody[Empty.type](httpResponse.getEntity).asInstanceOf[T]
      }

    private def toNameValuePairs(apacheHeaderElements: Array[ApacheHeaderElement]): List[NameValuePair] =
      apacheHeaderElements.map { header =>
        NameValuePair(header.getName, header.getValue)
      }.toList

    private def httpBody[T <: HttpBody : ApacheHttpBodyConverter](entity: HttpEntity): T =
      implicitly[ApacheHttpBodyConverter[T]] fromHttpEntity entity

  }

}
