package org.cartagena.tool.core.http.apache

import java.net.URI

import org.apache.http.ProtocolVersion
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicHttpResponse
import org.cartagena.tool.core.http.apache.ApacheHttpTestUtil.URL_STRING
import org.scalatest.{FlatSpec, Matchers}

class ApacheHttpMessageTest extends FlatSpec with Matchers {

  "ApacheHttpGet.getEntity" should "throw unsupported operation exception" in {
    // given
    val apacheHttpGet = new ApacheHttpGet(1L, new URI(URL_STRING))

    intercept[UnsupportedOperationException] {
      // when
      apacheHttpGet.getEntity
    }
  }

  "ApacheHttpGet.setEntity" should "throw unsupported operation exception" in {
    // given
    val apacheHttpGet = new ApacheHttpGet(1L, new URI(URL_STRING))
    val entity = new StringEntity("")

    intercept[UnsupportedOperationException] {
      // when
      apacheHttpGet setEntity entity
    }
  }

  "ApacheHttpPost.getURI" should "return HTTP POST request URI" in {
    // given
    val apacheHttpPost = new ApacheHttpPost(1L, new URI(URL_STRING))

    // when
    val actual = apacheHttpPost.getURI

    // then
    actual should be(new URI(URL_STRING))
  }

  "ApacheHttpPut.getURI" should "return HTTP PUT request URI" in {
    // given
    val apacheHttpPut = new ApacheHttpPut(1L, new URI(URL_STRING))

    // when
    val actual = apacheHttpPut.getURI

    // then
    actual should be(new URI(URL_STRING))
  }

  "ApacheHttpDelete.getMethod" should "return HTTP DELETE method" in {
    // given
    val apacheHttpDelete = new ApacheHttpDelete(1L, new URI(URL_STRING))

    // when
    val actual = apacheHttpDelete.getMethod

    // then
    actual should be("DELETE")
  }

  "ApacheHttpDelete.getURI" should "return HTTP POST request URI" in {
    // given
    val apacheHttpDelete = new ApacheHttpDelete(1L, new URI(URL_STRING))

    // when
    val actual = apacheHttpDelete.getURI

    // then
    actual should be(new URI(URL_STRING))
  }

  "ApacheHttpResponse.equals" should "return true when two ApacheHttpResponse objects are equal" in {
    // given
    val nativeResponse = new BasicHttpResponse(
      new ProtocolVersion("HTTP", 1, 1), 200, "OK")

    val response1 = new ApacheHttpResponse(id = 1L, nativeResponse)
    val response2 = new ApacheHttpResponse(id = 1L, nativeResponse)

    // then
    response1 equals response2 should be(true)
  }

  it should "return false when two ApacheHttpResponse objects have different IDs" in {
    // given
    val response1 = new ApacheHttpResponse(id = 1L, nativeResponse = null)
    val response2 = new ApacheHttpResponse(id = 2L, nativeResponse = null)

    // then
    response1 equals response2 should be(false)
  }

  it should "return false when two ApacheHttpResponse objects have different native response" in {
    // given
    val nativeResponse1 = new BasicHttpResponse(
      new ProtocolVersion("HTTP", 1, 1), 200, "OK")

    val nativeResponse2 = new BasicHttpResponse(
      new ProtocolVersion("HTTP", 1, 1), 201, "Created")

    val response1 = new ApacheHttpResponse(id = 1L, nativeResponse1)
    val response2 = new ApacheHttpResponse(id = 2L, nativeResponse2)

    // then
    response1 equals response2 should be(false)
  }

  it should "return false when comparing with non-ApacheHttpResponse object" in {
    // given
    val response = new ApacheHttpResponse(id = 1L, nativeResponse = null)

    // then
    response equals "unrelated" should be(false)
  }

}
