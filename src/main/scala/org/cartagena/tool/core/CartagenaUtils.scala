package org.cartagena.tool.core

import java.io.InputStream
import java.net.URL

import org.cartagena.tool.core.http._
import org.cartagena.tool.core.http.apache.inputStreamToString
import org.cartagena.tool.core.model._
import org.cartagena.tool.core.step.{RemoveJsonSerializers, ShutdownRestClient, StartRestClient}

import scala.collection.mutable
import scala.reflect.runtime.universe.TypeTag

object CartagenaUtils {

  implicit def stringToUrl(str: String): URL =
    new URL(str)

  implicit def nameValueTuplesToList(nameValuePairs: mutable.ListBuffer[(String, String)]): List[NameValuePair] =
    nameValuePairs.map {
      case (name, value) =>
        NameValuePair(name, value)
    }.toList

  implicit def inputStreamToJsonString(in: InputStream): JsonString =
    JsonString(inputStreamToString(in))

  implicit def shapelessSetupStepToSerialSetupStep(step: ShapelessSetupStep): SerialSetupStep =
    SerialSetupStep(step, () => EmptyStep)

  implicit def shapelessTestStepToSerialTestStep(step: ShapelessTestStep): SerialTestStep =
    SerialTestStep(step, () => EmptyStep)

  implicit def shapelessCleanupStepToSerialCleanupStep(step: ShapelessCleanupStep): SerialCleanupStep =
    SerialCleanupStep(step, () => EmptyStep)

  implicit class NameValuePairOps(nameValuePair: (String, String)) {

    def +(other: (String, String)): mutable.ListBuffer[(String, String)] =
      mutable.ListBuffer(nameValuePair, other)

  }

  implicit class NameValuePairsOps(nameValuePairs: mutable.ListBuffer[(String, String)]) {

    def +(other: (String, String)): mutable.ListBuffer[(String, String)] =
      nameValuePairs :+ other

  }

  implicit class ContextOps(context: Context) {

    def </[T: TypeTag](key: String): T =
      get(key)

    def get[T: TypeTag](key: String): T =
      ContextOpsBuilder[T]()
        .withContext(context)
        .withKey(key)
        .get

    def ~=>(key: String): WriteContext =
      create(key)

    def create(key: String): WriteContext =
      new CreateContextEntry(ContextOpsBuilder().withContext(context).withKey(key))

    def ~==>(key: String): UpdateContextEntry =
      update(key)

    def update(key: String): UpdateContextEntry =
      new UpdateContextEntry(ContextOpsBuilder().withContext(context).withKey(key))

    def <=~[T: TypeTag](key: String): T =
      remove(key)

    def remove[T: TypeTag](key: String): T =
      ContextOpsBuilder()
        .withContext(context)
        .withKey(key)
        .remove()

    trait WriteContext {

      def builder: ContextOpsBuilder[_, HasKey]

      def />[T: TypeTag](value: T): T =
        withValue(value)

      def withValue[T: TypeTag](value: T): T

    }

    class CreateContextEntry(val builder: ContextOpsBuilder[_, HasKey]) extends WriteContext {

      override def withValue[T: TypeTag](value: T): T =
        builder.asInstanceOf[ContextOpsBuilder[T, HasKey]]
          .withValue(value)
          .create()

    }

    class UpdateContextEntry(val builder: ContextOpsBuilder[_, HasKey]) extends WriteContext {

      override def withValue[T: TypeTag](value: T): T =
        builder.asInstanceOf[ContextOpsBuilder[T, HasKey]]
          .withValue(value)
          .update()

    }

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
