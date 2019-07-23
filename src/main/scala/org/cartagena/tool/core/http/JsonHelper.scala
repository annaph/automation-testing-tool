package org.cartagena.tool.core.http

import java.io.InputStream

import org.json4s.{Formats, Serializer}

trait JsonHelper {

  def useJsonFormats(formats: Formats): Unit

  def useJsonSerializer(serializer: Serializer[_]): Unit

  def useJsonSerializers(serializers: Iterable[Serializer[_]]): Unit

  def parse[T](json: JsonString)(implicit mf: Manifest[T]): T

  def parse[T](json: InputStream)(implicit mf: Manifest[T]): T

}
