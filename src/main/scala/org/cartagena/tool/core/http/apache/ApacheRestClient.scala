package org.cartagena.tool.core.http.apache

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.cartagena.tool.core.model.RestClient

trait ApacheRestClient extends RestClient {

  private var _isUp = false

  private var _client: HttpClient = _

  private[http] def client: HttpClient = _client

  private[http] def resetHttpClient(): Unit = {
    closeHttpClient()
    startHttpClient()
  }

  private[http] def startHttpClient(): Unit =
    if (!_isUp) create() else throw new Exception("Apache Http client already up!")

  private def create(): Unit = {
    _client = HttpClientBuilder.create().build()
    _isUp = true
  }

  private[http] def closeHttpClient(): Unit =
    if (_isUp) destroy() else throw new Exception("Apache Http client is not up!")

  private def destroy(): Unit = {
    _client = null
    _isUp = false
  }

  private[http] def isHttpClientUp: Boolean = _isUp

}
