package org.cartagena.tool.core.http

import org.cartagena.tool.core.http.HttpMethod.{HTTP_METHOD_GET, HTTP_METHOD_POST, HTTP_UNSUPPORTED_METHOD}
import org.scalatest.{FlatSpec, Matchers}

class HttpMethodTest extends FlatSpec with Matchers {

  "toPrettyString" should "prettify Get http method type" in {
    // given
    val method = Get

    // when
    val actual = method.toPrettyString

    // then
    actual should be(HTTP_METHOD_GET)
  }

  it should "prettify Post http method type" in {
    // given
    val method = Post

    // when
    val actual = method.toPrettyString

    // then
    actual should be(HTTP_METHOD_POST)
  }

  it should "prettify Unsupported http method type" in {
    // given
    val method = UnsupportedMethod

    // when
    val actual = method.toPrettyString

    // then
    actual should be(HTTP_UNSUPPORTED_METHOD)
  }

}
