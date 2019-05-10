package org.cartagena.tool.core

import java.net.URL

import org.cartagena.tool.core.http.json4s.Json4sFormats.{addSerializers, setFormats}
import org.cartagena.tool.core.http.json4s.Json4sFormatsRef.formatsRef
import org.cartagena.tool.core.model.{Context, EndStep, SerialTestStepX, TestStepX}
import org.json4s.{Formats, Serializer}

object CartagenaUtils {

  implicit def stringToUrl(str: String): URL =
    new URL(str)

  implicit def testStepToSerialTestStep(step: TestStepX): SerialTestStepX =
    SerialTestStepX(step, () => EndStep)

  implicit class ContextOperations(context: Context) {

    private var _key: Option[String] = None
    private var _isCreateNew = false

    def </[T: Manifest](key: String): T =
      context get[T] key

    def ~=>(key: String): ContextOperations =
      createKey(key)

    def createKey(key: String): ContextOperations = {
      updateInternalState(key, isCreateNew = true)
      this
    }

    private def updateInternalState(key: String, isCreateNew: Boolean): Unit = {
      _key = Some(key)
      _isCreateNew = isCreateNew
    }

    def ~==>(key: String): ContextOperations =
      updateKey(key)

    def updateKey(key: String): ContextOperations = {
      updateInternalState(key, isCreateNew = false)
      this
    }

    def <=~[T: Manifest](key: String): Unit =
      context remove[T] key

    def />[T: Manifest](value: T): Unit =
      withValue[T](value)

    def withValue[T: Manifest](value: T): Unit = _key match {
      case Some(key) if _isCreateNew =>
        context create[T](key, value)
      case Some(key) if !_isCreateNew =>
        context update[T](key, value)
      case None =>
        throw KeyNotSpecifiedException
    }

    object KeyNotSpecifiedException extends Exception("No key specified!")

  }

  def useJsonSerializer(serializer: Serializer[_]): Unit =
    addSerializers(formatsRef, List(serializer))

  def useJsonSerializers(serializers: Iterable[Serializer[_]]): Unit =
    addSerializers(formatsRef, serializers)

  def useJsonFormats(formats: Formats): Unit =
    setFormats(formatsRef, formats)

}
