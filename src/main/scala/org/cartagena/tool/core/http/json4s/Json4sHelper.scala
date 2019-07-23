package org.cartagena.tool.core.http.json4s

import java.io.InputStream

import org.cartagena.tool.core.http.{JsonHelper, JsonString}
import org.cartagena.tool.core.model.JsonHelperComponent
import org.json4s.{Formats, Serializer}

trait Json4sHelperComponent extends JsonHelperComponent {
  self: Json4sClientComponent with Json4sOperationsComponent =>

  private[core] val json4sHelper: JsonHelper

}

class Json4sHelperImpl(json4sClient: Json4sClient, json4sOperations: Json4sOperations)
  extends JsonHelper {

  override def useJsonFormats(formats: Formats): Unit =
    Json4sHelperImpl.useJsonFormats(json4sClient, formats)

  override def useJsonSerializer(serializer: Serializer[_]): Unit =
    Json4sHelperImpl.useJsonSerializers(json4sClient, serializer :: Nil)

  override def useJsonSerializers(serializers: Iterable[Serializer[_]]): Unit =
    Json4sHelperImpl.useJsonSerializers(json4sClient, serializers)

  override def parse[T](json: JsonString)(implicit mf: Manifest[T]): T =
    Json4sHelperImpl.parse(json4sOperations, json)(mf, json4sClient.formats)

  override def parse[T](json: InputStream)(implicit mf: Manifest[T]): T =
    Json4sHelperImpl.parse(json4sOperations, json)(mf, json4sClient.formats)

}

object Json4sHelperImpl {

  private def useJsonFormats(json4sClient: Json4sClient, formats: Formats): Unit =
    json4sClient setFormats formats

  private def useJsonSerializers(json4sClient: Json4sClient, serializers: Iterable[Serializer[_]]): Unit =
    json4sClient addSerializers serializers

  private def parse[T](json4sOperations: Json4sOperations, json: JsonString)
                      (implicit mf: Manifest[T], formats: Formats): T =
    json4sOperations parse json.str

  private def parse[T](json4sOperations: Json4sOperations, json: InputStream)
                      (implicit mf: Manifest[T], formats: Formats): T =
    json4sOperations parse json

}
