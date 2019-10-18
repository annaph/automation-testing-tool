package org.cartagena.tool.core.step

import org.cartagena.tool.core.agent.RestAgent
import org.cartagena.tool.core.model.ShapelessSetupStep

class StartRestClient extends ShapelessSetupStep with RestAgent {

  override val name: String = StartRestClient.STEP_NAME

  override def run(): Unit =
    restHelper.startRestClient()

}

object StartRestClient {

  val STEP_NAME = "Start REST client"

}
