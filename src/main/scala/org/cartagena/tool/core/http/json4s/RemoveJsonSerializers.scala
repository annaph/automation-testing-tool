package org.cartagena.tool.core.http.json4s

import org.cartagena.tool.core.http.json4s.Json4sFormats.setFormats
import org.cartagena.tool.core.http.json4s.Json4sFormatsRef.formatsRef
import org.cartagena.tool.core.model.ShapelessCleanupStep
import org.json4s.DefaultFormats

case class RemoveJsonSerializers() extends ShapelessCleanupStep {

  override def name: String = "Remove Json serializers"

  override def run(): Unit =
    setFormats(formatsRef, DefaultFormats)

}
