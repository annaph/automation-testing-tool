package org.cartagena.tool.core

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets

import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.PrettyPrintConstants.NEW_LINE
import org.cartagena.tool.core.http.{HttpRequest, HttpResponse, JsonString, Text}
import org.cartagena.tool.core.model._
import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

import scala.reflect.runtime.universe.typeTag
import scala.util.Success

class CartagenaUtilsTest extends FlatSpec with Matchers with MockitoSugar {

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

  "ContextOperations" should "return an entry value associated with a given key" in {
    // given
    val key = "key"
    val value = "value"

    val context = mock[Context]

    when(context.get(key)(typeTag[String]))
      .thenReturn(Success(value))

    // when
    val actual: String = context </[String] key

    // then
    actual should be(value)

    verify(context).get(key)(typeTag[String])
  }

  it should "create an entry with given value and key" in {
    // given
    val key = "key"
    val value = "value"

    val context = mock[Context]

    when(context.create(key, value)(typeTag[String]))
      .thenReturn(Success(value))

    // when
    val actual = context ~=> key />[String] value

    // then
    actual should be(value)

    verify(context).create(key, value)(typeTag[String])
  }

  it should "update an entry value associated with a given key" in {
    // given
    val key = "key"
    val value = "value"

    val context = mock[Context]

    when(context.update(key, value)(typeTag[String]))
      .thenReturn(Success(value))

    // when
    val actual = context ~==> key />[String] value

    // then
    actual should be(value)

    verify(context).update(key, value)(typeTag[String])
  }

  it should "remove an entry associated with a given key" in {
    // given
    val key = "key"
    val value = "value"

    val context = mock[Context]

    when(context.remove(key)(typeTag[String]))
      .thenReturn(Success(value))

    // when
    val actual: String = context <=~[String] key

    // then
    actual should be(value)

    verify(context).remove(key)(typeTag[String])
  }

  "SuiteReportPrinter" should "pretty print Suite Report" in {
    // given
    val suiteReport = mock[SuiteReport]
    val prettyStr = "pretty_string"

    when(suiteReport.toPrettyString)
      .thenReturn(prettyStr)

    val consoleOutput = new ByteArrayOutputStream()

    // when
    Console.withOut(consoleOutput) {
      suiteReport.print()
    }

    // then
    consoleOutput.toString should be(prettyStr + NEW_LINE)

    verify(suiteReport).toPrettyString
  }

  "StartRestClient" should "create StartRestClient step" in {
    // when
    val actual = StartRestClient

    // then
    actual shouldBe a[step.StartRestClient]
    actual.name should be(step.StartRestClient.STEP_NAME)
  }

  "ShutdownRestClient" should "create ShutdownRestClient step" in {
    // when
    val actual = ShutdownRestClient

    // then
    actual shouldBe a[step.ShutdownRestClient]
    actual.name should be(step.ShutdownRestClient.STEP_NAME)
  }

  "RemoveJsonSerializers" should "create RemoveJsonSerializers step" in {
    // when
    val actual = RemoveJsonSerializers

    // then
    actual shouldBe a[step.RemoveJsonSerializers]
    actual.name should be(step.RemoveJsonSerializers.STEP_NAME)
  }

  "print" should "pretty print HttpRequest message" in {
    // given
    val request = mock[HttpRequest[Text]]

    val prettyStr = "pretty_string"

    when(request.toPrettyString)
      .thenReturn(prettyStr)

    val consoleOutput = new ByteArrayOutputStream()

    // when
    Console.withOut(consoleOutput) {
      print(request)
    }

    // then
    consoleOutput.toString should be(prettyStr + NEW_LINE)

    verify(request).toPrettyString
  }

  it should "pretty print HttpResponse message" in {
    // given
    val response = mock[HttpResponse[Text]]

    val prettyStr = "pretty_string"

    when(response.toPrettyString)
      .thenReturn(prettyStr)

    val consoleOutput = new ByteArrayOutputStream()

    // when
    Console.withOut(consoleOutput) {
      print(response)
    }

    // then
    consoleOutput.toString should be(prettyStr + NEW_LINE)

    verify(response).toPrettyString
  }

}
