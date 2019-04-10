package org.cartagena.tool.core.http.apache

import org.apache.http.client.{HttpClient => Client}
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.protocol.{BasicHttpContext, HttpContext => Context}
import org.cartagena.tool.core.http.apache.ApacheHttpClient.{HttpClientIsNotUp, HttpClientIsUp}
import org.scalatest.{FlatSpec, Matchers}
import scalaz.effect.STRef

class ApacheHttpClientTest extends FlatSpec with Matchers {

  import ApacheHttpClientTest._

  "httpClient" should "return non null Http Client instance with success" in new StartedApacheHttpClient {
    // when
    val actual: Client = httpClient

    // then
    Option(actual).nonEmpty should be(true)
  }

  it should "throw exception when Http Client instance is null" in new ClosedApacheHttpClient {
    intercept[HttpClientIsNotUp.type] {
      // when
      httpClient
    }
  }

  "httpContext" should "return non null Http Context instance with success" in new StartedApacheHttpClient {
    // when
    val actual: Context = httpContext

    // then
    Option(actual).nonEmpty should be(true)
  }

  it should "throw exception when Http Context instance is null" in new ClosedApacheHttpClient {
    intercept[HttpClientIsNotUp.type] {
      // when
      httpContext
    }
  }

  "isHttpClientUp" should "return true if Http Client is up" in new StartedApacheHttpClient {
    // then
    isHttpClientUp should be(true)
  }

  it should "return false if Http Client is not up" in new ClosedApacheHttpClient {
    // then
    isHttpClientUp should be(false)
  }

  "startHttpClient" should "start Http Client with success" in new ClosedApacheHttpClient {
    // when
    startHttpClient()

    // then
    isHttpClientUp should be(true)
  }

  it should "throw exception if Http Client is already started" in new StartedApacheHttpClient {
    intercept[HttpClientIsUp.type] {
      // when
      startHttpClient()
    }
  }

  "closeHttpClient" should "close Http Client with success" in new StartedApacheHttpClient {
    // when
    closeHttpClient()

    // then
    isHttpClientUp should be(false)
  }

  it should "throw exception if Http Client is already closed" in new ClosedApacheHttpClient {
    intercept[HttpClientIsNotUp.type] {
      // when
      closeHttpClient()
    }
  }

  "resetHttpClient" should "reset Http Client with success" in new StartedApacheHttpClient {
    // when
    resetHttpClient()

    // then
    isHttpClientUp should be(true)
  }

  it should "throw exception if Http Client is already closed" in new ClosedApacheHttpClient {
    intercept[HttpClientIsNotUp.type] {
      // when
      resetHttpClient()
    }
  }

}

object ApacheHttpClientTest {

  import ApacheHttpClient.{ClientRef, ContextRef}

  trait StartedApacheHttpClient extends ApacheHttpClient {

    override private[apache] val clientRef: ClientRef =
      STRef[Nothing](Some(HttpClientBuilder.create().build()))

    override private[apache] val contextRef: ContextRef =
      STRef[Nothing](Some(new BasicHttpContext()))

  }

  trait ClosedApacheHttpClient extends ApacheHttpClient {

    override private[apache] val clientRef: ClientRef =
      STRef[Nothing](None)

    override private[apache] val contextRef: ContextRef =
      STRef[Nothing](None)

  }

}
