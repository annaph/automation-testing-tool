package org.cartagena.tool.core.http.json4s

import org.cartagena.tool.core.agent.JsonAgent
import org.cartagena.tool.core.http.json4s.RemoveJsonSerializers.STEP_NAME
import org.cartagena.tool.core.model.ShapelessCleanupStep
import org.json4s.DefaultFormats

case class RemoveJsonSerializers() extends ShapelessCleanupStep with JsonAgent {

  override def name: String = STEP_NAME

  override def run(): Unit =
    jsonHelper useJsonFormats DefaultFormats

}

object RemoveJsonSerializers {

  val STEP_NAME = "Remove Json serializers"

}
