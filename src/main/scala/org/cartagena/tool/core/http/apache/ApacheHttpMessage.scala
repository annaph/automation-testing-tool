package org.cartagena.tool.core.http.apache

import java.net.URI

import org.apache.http.client.methods.{HttpGet, HttpPost, HttpPut, HttpRequestBase}
import org.apache.http.{Header, HttpEntity, HttpResponse}

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

class ApacheHttpPut(val id: Long, val uri: URI) extends HttpPut with ApacheHttpRequest

class ApacheHttpResponse(val id: Long, response: HttpResponse) {

  def get: HttpResponse = response

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
    result = prime * result + response.hashCode()

    result
  }

}

object ApacheHttpResponse {

  def apply(id: Long, response: HttpResponse) =
    new ApacheHttpResponse(id, response)

}
