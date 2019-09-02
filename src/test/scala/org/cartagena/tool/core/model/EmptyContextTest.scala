package org.cartagena.tool.core.model

import org.scalatest.{FlatSpec, Matchers}

class EmptyContextTest extends FlatSpec with Matchers {

  "get" should "throw unsupported operation exception" in {
    // given
    val context = EmptyContext
    val key = "key"

    // when
    val actual = context get[String] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[UnsupportedOperationException]
  }

  "create" should "throw unsupported operation exception" in {
    // given
    val context = EmptyContext
    val key = "key"
    val value = "value"

    // when
    val actual = context create[String](key, value)

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[UnsupportedOperationException]
  }

  "update" should "throw unsupported operation exception" in {
    // given
    val context = EmptyContext
    val key = "key"
    val value = "value"

    // when
    val actual = context update[String](key, value)

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[UnsupportedOperationException]
  }

  "remove" should "throw unsupported operation exception" in {
    // given
    val context = EmptyContext
    val key = "key"

    // when
    val actual = context remove[String] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[UnsupportedOperationException]
  }

}
