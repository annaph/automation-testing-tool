package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.GetEntrySuiteContextTest.SuiteContextTest
import org.cartagena.tool.core.model.SuiteContextX.{EntriesRef, KeyNotPresentException, ValueTypeMismatchException}
import org.scalatest.{FlatSpec, Matchers}
import scalaz.effect.STRef

import scala.collection.mutable
import scala.reflect.runtime.universe.typeTag
import scala.util.Success

class GetEntrySuiteContextTest extends FlatSpec with Matchers {

  "get" should "succeed to return an entry value associated with a given key" in {
    // given
    val context = SuiteContextTest
    val key = "key1"

    // when
    val actual = context get[String] key

    // then
    actual should be(Success("value1"))
  }

  it should "fail to return an entry value where given key is not associated with any value" in {
    // given
    val context = SuiteContextTest
    val key = "non-existing"

    // when
    val actual = context get[String] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[KeyNotPresentException]
  }

  it should "fail to return an entry value associated with a given key where incorrect value type is specified" in {
    // given
    val context = SuiteContextTest
    val key = "key1"

    // when
    val actual = context get[Boolean] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[ValueTypeMismatchException]
  }

  it should "succeed to return a collection entry value associated with a given key" in {
    // given
    val context = SuiteContextTest
    val key = "key2"

    val expected = 1 :: Nil

    // when
    val actual = context get[List[Int]] key

    // then
    actual should be(Success(expected))
  }

  it should "fail to return a collection entry value where given key is not associated with any value" in {
    // given
    val context = SuiteContextTest
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
    val context = SuiteContextTest
    val key = "key2"

    // when
    val actual = context get[List[Boolean]] key

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[ValueTypeMismatchException]
  }

}

object GetEntrySuiteContextTest {

  case object SuiteContextTest extends SuiteContextX {

    override private[model] val entriesRef: EntriesRef = STRef[Nothing](mutable.Map(
      "key1" -> (typeTag[String] -> "value1"),
      "key2" -> (typeTag[List[Int]] -> (1 :: Nil))))

  }

}
