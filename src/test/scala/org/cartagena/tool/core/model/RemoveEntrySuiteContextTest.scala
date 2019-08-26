package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.SuiteContext.{KeyNotPresentException, ValueTypeMismatchException}
import org.cartagena.tool.core.model.SuiteContextTestUtil._
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Success

class RemoveEntrySuiteContextTest extends FlatSpec with Matchers {

  "remove" should "succeed to remove an entry associated with a given key" in {
    // given
    val context = SuiteContextTest
    val key = KEY_1

    // when
    val actual = context remove[String] key

    // then
    actual should be(Success(VALUE_1))
    context.get[String](key).failed.get shouldBe a[KeyNotPresentException]
  }

  it should "fail to remove an entry where given key is not associated with any value" in {
    // given
    val context = SuiteContextTest
    val key = "non-existing"

    // when
    val actual = context remove[String] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[KeyNotPresentException]
  }

  it should "fail to remove an entry associated with a given key where incorrect value type is specified" in {
    // given
    val context = SuiteContextTest
    val key = KEY_2

    // when
    val actual = context remove[Boolean] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[ValueTypeMismatchException]
  }

  it should "fail to remove an entry associated with a given key where incorrect collection value type is " +
    "specified" in {
    // given
    val context = SuiteContextTest
    val key = KEY_2

    // when
    val actual = context remove[List[Boolean]] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[ValueTypeMismatchException]
  }

  it should "succeed to remove an entry associated with a given key where correct collection value type is " +
    "specified" in {
    // given
    val context = SuiteContextTest
    val key = KEY_2

    // when
    val actual = context remove[List[Int]] key

    // then
    actual should be(Success(VALUE_2))
    context.get[String](key).failed.get shouldBe a[KeyNotPresentException]
  }

  it should "fail to remove an entry with collection value type where given key is not associated with any value" in {
    // given
    val context = SuiteContextTest
    val key = "non-existing"

    // when
    val actual = context remove[List[String]] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[KeyNotPresentException]
  }

}
