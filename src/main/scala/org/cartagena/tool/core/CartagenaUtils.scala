package org.cartagena.tool.core

import java.io.InputStream
import java.net.URL

import org.cartagena.tool.core.http.apache.inputStreamToString
import org.cartagena.tool.core.http.{HttpBody, HttpRequest, HttpResponse, JsonString}
import org.cartagena.tool.core.model._
import org.cartagena.tool.core.step.{RemoveJsonSerializers, ShutdownRestClient, StartRestClient}

import scala.reflect.runtime.universe.TypeTag
import scala.util.{Failure, Success, Try}

object CartagenaUtils {

  implicit def stringToUrl(str: String): URL =
    new URL(str)

  implicit def inputStreamToJsonString(in: InputStream): JsonString =
    JsonString(inputStreamToString(in))

  implicit def shapelessSetupStepToSerialSetupStep(step: ShapelessSetupStep): SerialSetupStep =
    SerialSetupStep(step, () => EmptyStep)

  implicit def shapelessTestStepToSerialTestStep(step: ShapelessTestStep): SerialTestStep =
    SerialTestStep(step, () => EmptyStep)

  implicit def shapelessCleanupStepToSerialCleanupStep(step: ShapelessCleanupStep): SerialCleanupStep =
    SerialCleanupStep(step, () => EmptyStep)

  implicit class ContextOperations(context: Context) {

    private var _key: Option[String] = None
    private var _isCreateNew = false

    def </[T: TypeTag](key: String): T = {
      //get(key)
     ContextOperationsBuilder().withContext(context).withKey(key).get
    }

    def get[T: TypeTag](key: String): T =
      extractValue(context.get[T](key))

    def ~=>(key: String): ContextOperations =
      create(key)

    def create(key: String): ContextOperations = {
      _key = Some(key)
      _isCreateNew = true

      this
    }

    def ~==>(key: String): ContextOperations =
      update(key)

    def update(key: String): ContextOperations = {
      _key = Some(key)
      _isCreateNew = false

      this
    }

    def <=~[T: TypeTag](key: String): Unit =
      remove(key)

    def remove[T: TypeTag](key: String): T =
      extractValue(context.remove[T](key))

    def />[T: TypeTag](value: T): T =
      withValue[T](value)

    def withValue[T: TypeTag](value: T): T = _key match {
      case Some(key) if _isCreateNew =>
        extractValue(context.create[T](key, value))
      case Some(key) if !_isCreateNew =>
        extractValue(context.update[T](key, value))
      case None =>
        throw KeyNotSpecifiedException
    }

    private def extractValue[T](value: Try[T]): T =
      value match {
        case Success(v) =>
          v
        case Failure(e) =>
          throw e
      }

    object KeyNotSpecifiedException extends Exception("No key specified!")

  }

  implicit class SuiteReportPrinter(suiteReport: SuiteReport) {

    def print(): Unit =
      println(suiteReport.toPrettyString)

  }

  def StartRestClient: StartRestClient =
    new StartRestClient()

  def ShutdownRestClient: ShutdownRestClient =
    new ShutdownRestClient()

  def RemoveJsonSerializers: RemoveJsonSerializers =
    new RemoveJsonSerializers()

  def print[T <: HttpBody](request: HttpRequest[T]): Unit =
    println(request.toPrettyString)

  def print[T <: HttpBody](response: HttpResponse[T]): Unit =
    println(response.toPrettyString)

}
