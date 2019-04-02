package org.cartagena.tool.core.http.apache

import java.net.URL

import org.apache.http.Header

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.{HttpGet, HttpPost, HttpPut}
import org.apache.http.client.protocol.HttpClientContext.COOKIE_STORE
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.protocol.HttpContext
import org.apache.http.{HttpEntity, HttpResponse}
import org.cartagena.tool.core.model.HttpOperations
import java.net.URI

sealed trait ApacheHttpRequest {

  def id: Long

  def getURI: URI

  def getAllHeaders: Array[Header]

  def getEntity: HttpEntity

  def addHeader(name: String, value: String): Unit

  def setEntity(entity: HttpEntity): Unit

}

class ApacheHttpGetRequest(val id: Long, val uri: URI) extends HttpGet(uri) with ApacheHttpRequest {

  override def getEntity: HttpEntity =
    throw new UnsupportedOperationException("HTTP GET request has no entity!")

  override def setEntity(entity: HttpEntity): Unit =
    throw new UnsupportedOperationException("Cannot set entity to HTTP GET request!")

}

class ApacheHttpPostRequest(val id: Long, val uri: URI) extends HttpPost with ApacheHttpRequest {

  override def getURI: URI =
    uri

}

class ApacheHttpPutRequest(val id: Long, val uri: URI) extends HttpPut with ApacheHttpRequest

case class ApacheHttpResponse(id: Long, response: HttpResponse)

trait ApacheHttpOperations extends HttpOperations {

  private[apache] def executePost(url: URL, entity: HttpEntity, headers: Map[String, String], params: Map[String, String])
                                 (implicit client: HttpClient, context: HttpContext): HttpResponse = {
    val uriBuilder = new URIBuilder(url.toURI)

    params.foreach {
      case (name, value) =>
        uriBuilder setParameter(name, value)
    }

    val request = new HttpPost(uriBuilder.build())

    headers.foreach {
      case (name, value) =>
        request addHeader(name, value)
    }

    request setEntity entity

    client execute(request, context)
  }

  private[apache] def addToCookieStore(elementName: String, elementValue: String, domain: String, path: String)
                                      (implicit client: HttpClient, context: HttpContext): Unit = {
    val cookie = new BasicClientCookie(elementName, elementValue)
    cookie setDomain domain
    cookie setPath path

    val cookieStore = (context getAttribute COOKIE_STORE).asInstanceOf[BasicCookieStore]
    cookieStore addCookie cookie
  }

}
