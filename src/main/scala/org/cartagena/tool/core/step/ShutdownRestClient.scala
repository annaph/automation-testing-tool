package org.cartagena.tool.core.step

import org.cartagena.tool.core.agent.RestAgent
import org.cartagena.tool.core.model.ShapelessCleanupStep
import org.cartagena.tool.core.step.ShutdownRestClient.STEP_NAME

class ShutdownRestClient extends ShapelessCleanupStep with RestAgent {

  override val name: String = STEP_NAME

  override def run(): Unit =
    restHelper.shutdownRestClient()

}

object ShutdownRestClient {

  val STEP_NAME = "Shutdown REST client"

}
