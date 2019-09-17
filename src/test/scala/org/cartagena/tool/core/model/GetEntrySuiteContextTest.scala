package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.SuiteContext.{KeyNotPresentException, ValueTypeMismatchException}
import org.cartagena.tool.core.model.SuiteContextTestUtil._
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Success

class GetEntrySuiteContextTest extends FlatSpec with Matchers {

  "get" should "succeed to return an entry value associated with a given key" in {
    // given
    val context = MySuiteContextTest()
    val key = KEY_1

    // when
    val actual = context get[String] key

    // then
    actual should be(Success(VALUE_1))
  }

  it should "fail to return an entry value where given key is not associated with any value" in {
    // given
    val context = MySuiteContextTest()
    val key = "non-existing"

    // when
    val actual = context get[String] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[KeyNotPresentException]
  }

  it should "fail to return an entry value associated with a given key where incorrect value type is specified" in {
    // given
    val context = MySuiteContextTest()
    val key = KEY_1

    // when
    val actual = context get[Boolean] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[ValueTypeMismatchException]
  }

  it should "succeed to return a collection entry value associated with a given key" in {
    // given
    val context = MySuiteContextTest()
    val key = KEY_2

    // when
    val actual = context get[List[Int]] key

    // then
    actual should be(Success(VALUE_2))
  }

  it should "fail to return a collection entry value where given key is not associated with any value" in {
    // given
    val context = MySuiteContextTest()
    val key = "non-existing"

    // when
    val actual = context get[List[String]] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[KeyNotPresentException]
  }

  it should "fail to return a collection entry value associated with a given key where incorrect collection value " +
    "type is specified" in {
    // given
    val context = MySuiteContextTest()
    val key = KEY_2

    // when
    val actual = context get[List[Boolean]] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[ValueTypeMismatchException]
  }

}
