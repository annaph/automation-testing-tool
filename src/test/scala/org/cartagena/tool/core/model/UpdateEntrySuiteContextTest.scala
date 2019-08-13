package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.SuiteContextX.{EntriesRef, KeyNotPresentException, ValueTypeMismatchException}
import org.cartagena.tool.core.model.UpdateEntrySuiteContextTest.SuiteContextTest
import org.scalatest.{FlatSpec, Matchers}
import scalaz.effect.STRef

import scala.collection.mutable
import scala.reflect.runtime.universe.typeTag
import scala.util.Success

class UpdateEntrySuiteContextTest extends FlatSpec with Matchers {

  "update" should "succeed to update an entry value associated with a given key" in {
    // given
    val context = SuiteContextTest
    val key = "key1"
    val value = "newValue1"

    // when
    val actual = context update[String](key, value)

    // then
    actual should be(Success(value))
    context.get[String](key) should be(Success(value))
  }

  it should "fail to update an entry value when given key is not associated with any value" in {
    // given
    val context = SuiteContextTest
    val key = "non-existing"
    val value = "newValue"

    // when
    val actual = context update[String](key, value)

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[KeyNotPresentException]
  }

  it should "fail to update an entry value associated with a given key where incorrect value type is specified" in {
    // given
    val context = SuiteContextTest
    val key = "key1"
    val value = true

    // when
    val actual = context update[Boolean](key, value)

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[ValueTypeMismatchException]
  }

  it should "succeed to update a collection entry value associated with a given key" in {
    // given
    val context = SuiteContextTest
    val key = "key2"
    val value = 2 :: Nil

    // when
    val actual = context update[List[Int]](key, value)

    // then
    actual should be(Success(value))
    context.get[List[Int]](key) should be(Success(value))
  }

  it should "fail to update a collection entry value where given key is not associated with any value" in {
    // given
    val context = SuiteContextTest
    val key = "non-existing"
    val value = "newValue" :: Nil

    // when
    val actual = context update[List[String]](key, value)

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[KeyNotPresentException]
  }

  it should "fail to update a collection entry value associated with a given key where incorrect collection value " +
    "type is specified" in {
    // given
    val context = SuiteContextTest
    val key = "key2"
    val value = true :: Nil

    // when
    val actual = context update[List[Boolean]](key, value)

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[ValueTypeMismatchException]
  }

}

object UpdateEntrySuiteContextTest {

  case object SuiteContextTest extends SuiteContextX {

    override private[model] val entriesRef: EntriesRef = STRef[Nothing](mutable.Map(
      "key1" -> (typeTag[String] -> "value1"),
      "key2" -> (typeTag[List[Int]] -> (1 :: Nil))))

  }

}
