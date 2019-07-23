package org.cartagena.tool.core.http.json4s

import org.cartagena.tool.core.agent.JsonAgent
import org.cartagena.tool.core.model.ShapelessCleanupStep
import org.json4s.DefaultFormats

case class RemoveJsonSerializers() extends ShapelessCleanupStep with JsonAgent {

  override def name: String = "Remove Json serializers"

  override def run(): Unit =
    jsonHelper useJsonFormats DefaultFormats

}
