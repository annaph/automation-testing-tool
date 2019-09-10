package org.cartagena.tool.core

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import org.cartagena.tool.core.CartagenaUtils.{ContextOperations, inputStreamToJsonString, shapelessCleanupStepToSerialCleanupStep, shapelessSetupStepToSerialSetupStep, shapelessTestStepToSerialTestStep, stringToUrl}
import org.cartagena.tool.core.http.JsonString
import org.cartagena.tool.core.model._
import org.scalatest.{FlatSpec, Matchers}

class CartagenaUtilsTest extends FlatSpec with Matchers {

  "stringToUrl" should "convert String to URL" in {
    // given
    val str = "http://www.google.com/"

    // when
    val actual = stringToUrl(str)

    // then
    actual.toString should be(str)
  }

  "inputStreamToJsonString" should "convert InputStream to JsonString" in {
    // given
    val in = new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8.name()))

    // when
    val actual = inputStreamToJsonString(in)

    // then
    actual should be(JsonString("{}"))
  }

  "shapelessSetupStepToSerialSetupStep" should "convert ShapelessSetupStep to SerialSetupStep" in {
    // given
    val step = new ShapelessSetupStep {
      override def name: String = "Shapeless Setup Step"

      override def run(): Unit = {}
    }

    // when
    val actual = shapelessSetupStepToSerialSetupStep(step)

    // then
    actual.left should be(step)
    actual.right() should be(EmptyStep)
  }

  "shapelessTestStepToSerialTestStep" should "convert ShapelessTestStep to SerialTestStep" in {
    // given
    val step = new ShapelessTestStep {
      override def name: String = "Shapeless Test Step"

      override def run(): Unit = {}
    }

    // when
    val actual = shapelessTestStepToSerialTestStep(step)

    // then
    actual.left should be(step)
    actual.right() should be(EmptyStep)
  }

  "shapelessCleanupStepToSerialCleanupStep" should "convert ShapelessCleanupStep to SerialCleanupStep" in {
    // given
    val step = new ShapelessCleanupStep {
      override def name: String = "Shapeless Cleanup Step"

      override def run(): Unit = {}
    }

    // when
    val actual = shapelessCleanupStepToSerialCleanupStep(step)

    // then
    actual.left should be(step)
    actual.right() should be(EmptyStep)
  }

  "ContextOperations.get" should "return an entry value associated with a given key" in {
    // given
    val key = "key"
    val value = "value"

    val context = Context(key -> value)

    // when
    val actual: String = context </[String] key

    // then
    actual should be(value)
  }

  it should "throw an exception" in {
    // given
    val key = "key"
    val context = Context.empty

    intercept[Exception] {
      // when
      context </ key
    }
  }

}
