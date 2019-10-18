package org.cartagena.tool.core.step

import org.cartagena.tool.core.agent.JsonAgent
import org.cartagena.tool.core.model.ShapelessCleanupStep
import org.json4s.DefaultFormats

class RemoveJsonSerializers extends ShapelessCleanupStep with JsonAgent {

  override def name: String = RemoveJsonSerializers.STEP_NAME

  override def run(): Unit =
    jsonHelper useJsonFormats DefaultFormats

}

object RemoveJsonSerializers {

  val STEP_NAME = "Remove Json serializers"

}
