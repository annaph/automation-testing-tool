package org.cartagena.tool.core.http.apache

import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.protocol.BasicHttpContext
import org.cartagena.tool.core.http.apache.ApacheHttpClientRefs.{ClientRef, ContextRef}
import org.cartagena.tool.core.http.apache.StartedApacheHttpClientTest.StartedApacheHttpClient
import org.cartagena.tool.core.registry.ApacheRestRegistryTest
import org.scalatest.{FlatSpec, Matchers}
import scalaz.effect.STRef

class StartedApacheHttpClientTest extends FlatSpec with Matchers with ApacheRestRegistryTest {

  override private[core] val apacheHttpClient =
    new StartedApacheHttpClient

  "get" should "return non null Apache Http Client instance with success" in {
    // when
    val actual = apacheHttpClient.get

    // then
    Option(actual).nonEmpty should be(true)
  }

  "context" should "return non null Apache Http Context instance with success" in {
    // when
    val actual = apacheHttpClient.context

    // then
    Option(actual).nonEmpty should be(true)
  }

  "isUp" should "return true if Http Client is up" in {
    // then
    apacheHttpClient.isUp should be(true)
  }

  "start" should "throw exception if Apache Http Client is already started" in {
    intercept[HttpClientIsUp.type] {
      // when
      apacheHttpClient.start()
    }
  }

  "reset" should "reset Apache Http Client with success" in {
    // when
    apacheHttpClient.reset()

    // then
    apacheHttpClient.isUp should be(true)
  }

  "close" should "close Apache Http Client with success" in {
    // when
    apacheHttpClient.close()

    // then
    apacheHttpClient.isUp should be(false)
  }

}

object StartedApacheHttpClientTest {

  class StartedApacheHttpClient extends ApacheHttpClientImpl {

    override private[apache] val clientRef: ClientRef =
      STRef[Nothing](Some(HttpClientBuilder.create().build()))

    override private[apache] val contextRef: ContextRef =
      STRef[Nothing](Some(new BasicHttpContext()))

  }

}
