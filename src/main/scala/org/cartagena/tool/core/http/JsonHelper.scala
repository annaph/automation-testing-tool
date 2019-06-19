package org.cartagena.tool.core.http

import java.io.InputStream

import org.cartagena.tool.core.model.{NoNativeClientComponent, NoOperations}

trait JsonHelper extends NoNativeClientComponent with NoOperations {

  def parse[T: Manifest](json: JsonString): T

  def parse[T: Manifest](json: InputStream): T

  def toJsonString(in: InputStream): JsonString

}

trait JsonHelperComponent {

  val jsonHelper: JsonHelper

}
