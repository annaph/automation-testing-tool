package org.cartagena.tool.core.http.apache

import org.apache.http.client.protocol.HttpClientContext.COOKIE_STORE
import org.apache.http.impl.client.{BasicCookieStore, HttpClientBuilder}
import org.apache.http.protocol.{BasicHttpContext, HttpContext}
import org.cartagena.tool.core.model.HttpClient

trait ApacheHttpClient extends HttpClient {

  private[http] def httpClient: org.apache.http.client.HttpClient =
    ApacheHttpClient.httpClient

  private[http] def httpContext: HttpContext =
    ApacheHttpClient.httpContext

  private[http] def startHttpClient(): Unit =
    ApacheHttpClient.startHttpClient()

  private[http] def resetHttpClient(): Unit =
    ApacheHttpClient.resetHttpClient()

  private[http] def closeHttpClient(): Unit =
    ApacheHttpClient.closeHttpClient()

  private[http] def isHttpClientUp: Boolean =
    ApacheHttpClient.isHttpClientUp

}

object ApacheHttpClient {

  private var _isUp = false

  private var _client: org.apache.http.client.HttpClient = _

  private var _context: HttpContext = _

  private def httpClient: org.apache.http.client.HttpClient =
    _client

  private def httpContext: HttpContext =
    _context

  private def resetHttpClient(): Unit = {
    closeHttpClient()
    startHttpClient()
  }

  private def startHttpClient(): Unit =
    if (!_isUp) create() else throw new Exception("Apache Http client already up!")

  private def create(): Unit = {
    _client = HttpClientBuilder.create().build()

    _context = new BasicHttpContext()
    _context setAttribute(COOKIE_STORE, new BasicCookieStore())

    _isUp = true
  }

  private def closeHttpClient(): Unit =
    if (_isUp) destroy() else throw new Exception("Apache Http client is not up!")

  private def destroy(): Unit = {
    _client = null
    _context = null

    _isUp = false
  }

  private def isHttpClientUp: Boolean =
    _isUp

}
