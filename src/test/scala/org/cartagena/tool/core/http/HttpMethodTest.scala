package org.cartagena.tool.core.http

import org.cartagena.tool.core.http.HttpMethod.{HTTP_METHOD_DELETE, HTTP_METHOD_GET, HTTP_METHOD_POST}
import org.scalatest.{FlatSpec, Matchers}

class HttpMethodTest extends FlatSpec with Matchers {

  "toPrettyString" should "prettify Get Http method type" in {
    // given
    val method = Get

    // when
    val actual = method.toPrettyString

    // then
    actual should be(HTTP_METHOD_GET)
  }

  it should "prettify Post Http method type" in {
    // given
    val method = Post

    // when
    val actual = method.toPrettyString

    // then
    actual should be(HTTP_METHOD_POST)
  }

  it should "prettify Delete Http method type" in {
    // given
    val method = Delete

    // when
    val actual = method.toPrettyString

    // then
    actual should be(HTTP_METHOD_DELETE)
  }

}
