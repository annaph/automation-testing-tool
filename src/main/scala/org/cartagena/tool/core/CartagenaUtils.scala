package org.cartagena.tool.core

import java.net.URL

import org.cartagena.tool.core.http.{HttpBody, HttpRequest, HttpResponse}
import org.cartagena.tool.core.model._
import org.cartagena.tool.core.step.{RemoveJsonSerializers, ShutdownRestClient, StartRestClient}

import scala.reflect.runtime.universe.TypeTag
import scala.util.{Failure, Success, Try}

object CartagenaUtils {

  implicit def stringToUrl(str: String): URL =
    new URL(str)

  implicit def setupStepToSerialSetupStep(step: ShapedSetupStep): SerialSetupStep =
    SerialSetupStep(step, () => EmptyStep)

  implicit def testStepToSerialTestStep(step: ShapedTestStep): SerialTestStep =
    SerialTestStep(step, () => EmptyStep)

  implicit def cleanupStepToSerialCleanupStep(step: ShapedCleanupStep): SerialCleanupStep =
    SerialCleanupStep(step, () => EmptyStep)

  implicit class ContextOperations(context: ContextX) {

    private var _key: Option[String] = None
    private var _isCreateNew = false

    def </[T: TypeTag](key: String): T =
      get(key)

    def get[T: TypeTag](key: String): T =
      context.get[T](key) match {
        case Success(value) =>
          value
        case Failure(e) =>
          throw e
      }

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

    def remove[T: TypeTag](key: String): Unit =
      context.remove[T](key) match {
        case Success(_) =>
          ()
        case Failure(e) =>
          throw e
      }

    def />[T: TypeTag](value: T): Unit =
      withValue[T](value)

    def withValue[T: TypeTag](value: T): Unit = _key match {
      case Some(key) if _isCreateNew =>
        context.create[T](key, value) match {
          case Success(_) =>
            ()
          case Failure(e) =>
            throw e
        }
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
