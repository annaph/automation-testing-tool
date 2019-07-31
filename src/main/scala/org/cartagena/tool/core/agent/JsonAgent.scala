package org.cartagena.tool.core.agent

import org.cartagena.tool.core.http.JsonHelper
import org.cartagena.tool.core.registry.Json4sRegistry

trait JsonAgent extends Json4sRegistry {

  val jsonHelper: JsonHelper = json4sHelper

}
