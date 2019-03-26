package org.cartagena.tool.core.http.json4s

import java.io.InputStream
import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.cartagena.tool.core.http.{JsonHelper, JsonString}
import org.json4s.jackson.parseJson
import org.json4s.{DefaultFormats, Formats, StreamInput}

trait Json4sHelper extends JsonHelper {

  private lazy val _formats = DefaultFormats

  override def parse[T: Manifest](json: JsonString): T =
    parseJson(json.str).extract[T](formats, implicitly[Manifest[T]])

  def formats: Formats = _formats

  override def parse[T: Manifest](json: InputStream): T =
    parseJson(StreamInput(json)).extract[T](formats, implicitly[Manifest[T]])

  override def toJsonString(in: InputStream): JsonString =
    JsonString(IOUtils toString(in, StandardCharsets.UTF_8.name()))

}
