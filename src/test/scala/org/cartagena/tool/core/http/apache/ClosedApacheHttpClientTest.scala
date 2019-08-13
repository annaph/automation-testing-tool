package org.cartagena.tool.core.http.apache

import org.cartagena.tool.core.http.apache.ApacheHttpClientRefs.{ClientRef, ContextRef}
import org.cartagena.tool.core.http.apache.ClosedApacheHttpClientTest.ClosedApacheHttpClient
import org.cartagena.tool.core.registry.ApacheRestRegistryTest
import org.scalatest.{FlatSpec, Matchers}
import scalaz.effect.STRef

class ClosedApacheHttpClientTest extends FlatSpec with Matchers with ApacheRestRegistryTest {

  override private[core] val apacheHttpClient =
    new ClosedApacheHttpClient

  "get" should "throw exception when Apache Http Client instance is null" in {
    intercept[HttpClientIsNotUp.type] {
      // when
      apacheHttpClient.get
    }
  }

  "context" should "throw exception when Apache Http Context instance is null" in {
    intercept[HttpClientIsNotUp.type] {
      // when
      apacheHttpClient.context
    }
  }

  "isUp" should "return false if Apache Http Client is not up" in {
    // then
    apacheHttpClient.isUp should be(false)
  }

  "reset" should "throw exception if Apache Http Client is already closed" in {
    intercept[HttpClientIsNotUp.type] {
      // when
      apacheHttpClient.reset()
    }
  }

  "close" should "throw exception if Apache Http Client is already closed" in {
    intercept[HttpClientIsNotUp.type] {
      // when
      apacheHttpClient.close()
    }
  }

  "start" should "start Apache Http Client with success" in {
    // when
    apacheHttpClient.start()

    // then
    apacheHttpClient.isUp should be(true)
  }

}

object ClosedApacheHttpClientTest {

  class ClosedApacheHttpClient extends ApacheHttpClientImpl {

    override private[apache] val clientRef: ClientRef =
      STRef[Nothing](None)

    override private[apache] val contextRef: ContextRef =
      STRef[Nothing](None)

  }

}
