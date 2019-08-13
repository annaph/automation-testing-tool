package org.cartagena.tool.core.step

import org.cartagena.tool.core.agent.RestAgent
import org.cartagena.tool.core.model.ShapelessSetupStep
import org.cartagena.tool.core.step.StartRestClient.STEP_NAME

class StartRestClient extends ShapelessSetupStep with RestAgent {

  override val name: String = STEP_NAME

  override def run(): Unit =
    restHelper.startRestClient()

}

object StartRestClient {

  val STEP_NAME = "Start REST client"

}
