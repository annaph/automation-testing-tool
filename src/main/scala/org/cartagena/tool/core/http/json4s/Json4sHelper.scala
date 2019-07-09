package org.cartagena.tool.core.http.json4s

import java.io.InputStream

import org.cartagena.tool.core.http.{JsonHelper, JsonString, inputStreamToString}
import org.json4s.StreamInput
import org.json4s.jackson.parseJson

class Json4sHelper extends JsonHelper with Json4sFormats {

  override def parse[T: Manifest](json: JsonString): T =
    parseJson(json.str).extract[T](formats, implicitly[Manifest[T]])

  override def parse[T: Manifest](json: InputStream): T =
    parseJson(StreamInput(json)).extract[T](formats, implicitly[Manifest[T]])

  override def toJsonString(in: InputStream): JsonString =
    JsonString(in)

}

object Json4sHelper {

  def apply(): Json4sHelper = new Json4sHelper()

}
