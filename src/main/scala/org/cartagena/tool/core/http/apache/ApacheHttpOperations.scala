package org.cartagena.tool.core.http.apache

import org.apache.http.{HeaderElement, HttpEntity, HttpResponse}
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.protocol.HttpContext
import org.cartagena.tool.core.http.Cookie
import org.cartagena.tool.core.model.HttpOperations
import java.net.{URI, URL}

import org.apache.http.client.protocol.HttpClientContext.COOKIE_STORE
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.cookie.BasicClientCookie

trait ApacheHttpOperations extends HttpOperations {

  private[http] def addToCookieStore(elementName: String, elementValue: String, domain: String, path: String)
                                    (implicit client: HttpClient, context: HttpContext): Unit = {
    val cookie = new BasicClientCookie(elementName, elementValue)
    cookie setDomain domain
    cookie setPath path

    val cookieStore = (context getAttribute COOKIE_STORE).asInstanceOf[BasicCookieStore]
    cookieStore addCookie cookie
  }

  private[http] def executePost(url: URL, entity: HttpEntity, params: Map[String, String] = Map.empty)
                               (implicit client: HttpClient, context: HttpContext): HttpResponse = {
    val uriBuilder = new URIBuilder(url.toURI)

    params.foreach {
      case (name, value) =>
        uriBuilder setParameter(name, value)
    }

    val request = new HttpPost(uriBuilder.build())
    request setEntity entity

    client execute(request, context)
  }

}
