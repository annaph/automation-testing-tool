package org.cartagena.tool.core.http.json4s

import java.io.InputStream

import org.cartagena.tool.core.model.JsonOperationsComponent
import org.json4s.jackson.parseJson
import org.json4s.{Formats, StreamInput}

trait Json4sOperations {

  def parse[T: Manifest](json: String)(implicit formats: Formats): T

  def parse[T: Manifest](json: InputStream)(implicit formats: Formats): T

}

trait Json4sOperationsComponent extends JsonOperationsComponent {

  private[core] val json4sOperations: Json4sOperations

}

class Json4sOperationsImpl extends Json4sOperations {

  override def parse[T: Manifest](json: String)(implicit formats: Formats): T =
    Json4sOperationsImpl.parseJsonString(json)

  override def parse[T: Manifest](json: InputStream)(implicit formats: Formats): T =
    Json4sOperationsImpl.parseJsonInputStream(json)

}

object Json4sOperationsImpl {

  private def parseJsonString[T: Manifest](json: String)(implicit formats: Formats): T =
    parseJson(json).extract[T]

  private def parseJsonInputStream[T: Manifest](json: InputStream)(implicit formats: Formats): T =
    parseJson(StreamInput(json)).extract[T]

}
