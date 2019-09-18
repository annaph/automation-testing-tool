package org.cartagena.tool.core.model

import org.scalatest.{FlatSpec, Matchers}

class ContextTest extends FlatSpec with Matchers {

  "empty" should "create empty Context" in {
    // when
    val actual = Context.empty

    // then
    actual.isEmpty should be(true)
  }

  "apply" should "create Context with no entries" in {
    // when
    val actual = Context.apply()

    // then
    actual.isEmpty should be(true)
  }

  it should "create Context with one entry" in {
    // given
    val key = "key"
    val value = "value"

    // when
    val actual = Context(key -> value)

    // then
    actual.nonEmpty should be(true)
    actual.size should be(1)

    actual.get[String](key).get should be(value)
  }

  it should "create Context with two entries" in {
    // given
    val key1 = "key"
    val value1 = "value"

    val key2 = "key2"
    val value2 = "value2"

    // when
    val actual = Context(key1 -> value1, key2 -> value2)

    // then
    actual.nonEmpty should be(true)
    actual.size should be(2)

    actual.get[String](key1).get should be(value1)
    actual.get[String](key2).get should be(value2)
  }

  it should "create Context with three entries" in {
    // given
    val key1 = "key"
    val value1 = "value"

    val key2 = "key2"
    val value2 = "value2"

    val key3 = "key3"
    val value3 = "value3"

    // when
    val actual = Context(key1 -> value1, key2 -> value2, key3 -> value3)

    // then
    actual.nonEmpty should be(true)
    actual.size should be(3)

    actual.get[String](key1).get should be(value1)
    actual.get[String](key2).get should be(value2)
    actual.get[String](key3).get should be(value3)
  }

}
