package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.CreateEntrySuiteContextTest.SuiteContextTest
import org.cartagena.tool.core.model.SuiteContextX.{EntriesRef, KeyAlreadyPresentException}
import org.scalatest.{FlatSpec, Matchers}
import scalaz.effect.STRef

import scala.collection.mutable
import scala.reflect.runtime.universe.typeTag
import scala.util.Success

class CreateEntrySuiteContextTest extends FlatSpec with Matchers {

  "create" should "succeed to create an entry with given value and key" in {
    // given
    val context = SuiteContextTest
    val key = "key3"
    val value = "value3"

    // when
    val actual = context create[String](key, value)

    // then
    actual should be(Success(value))
    context.get[String](key) should be(Success(value))
  }

  it should "fail to create an entry with given value and key where key is already associated with some value" in {
    // given
    val context = SuiteContextTest
    val key = "key1"
    val value = "value1"

    // when
    val actual = context create[String](key, value)

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[KeyAlreadyPresentException]
  }

  it should "succeed to create an entry with given collection value and key" in {
    // given
    val context = SuiteContextTest
    val key = "key4"
    val value = "value4" :: Nil

    // when
    val actual = context create[List[String]](key, value)

    // then
    actual should be(Success(value))
    context.get[List[String]](key) should be(Success(value))
  }

  it should "fail to create an entry with given collection value and key where key is already associated with some " +
    "value" in {
    // given
    val context = SuiteContextTest
    val key = "key1"
    val value = "value1" :: Nil

    // when
    val actual = context create[List[String]](key, value)

    // then
    actual.isFailure should be(true)
    actual.failed.get shouldBe a[KeyAlreadyPresentException]
  }

}

object CreateEntrySuiteContextTest {

  case object SuiteContextTest extends SuiteContextX {

    override private[model] val entriesRef: EntriesRef = STRef[Nothing](mutable.Map(
      "key1" -> (typeTag[String] -> "value1"),
      "key2" -> (typeTag[List[Int]] -> (1 :: Nil))))

  }

}
