package org.cartagena.tool.core.model

import org.scalatest.{FlatSpec, Matchers}

class EmptyContextTest extends FlatSpec with Matchers {

  "get" should "fail to return an entry value" in {
    // given
    val context = EmptyContext
    val key = "key"

    // when
    val actual = context get[String] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[UnsupportedOperationException]
  }

  "create" should "fail to create an entry" in {
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

  "update" should "fail to update an entry value" in {
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

  "remove" should "fail to remove an entry" in {
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
