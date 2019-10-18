package org.cartagena.tool.core.step

import org.cartagena.tool.core.agent.RestAgent
import org.cartagena.tool.core.model.ShapelessCleanupStep

class ShutdownRestClient extends ShapelessCleanupStep with RestAgent {

  override val name: String = ShutdownRestClient.STEP_NAME

  override def run(): Unit =
    restHelper.shutdownRestClient()

}

object ShutdownRestClient {

  val STEP_NAME = "Shutdown REST client"

}
