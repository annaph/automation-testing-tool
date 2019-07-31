package org.cartagena.tool.core.agent

import org.cartagena.tool.core.http.RestHelper
import org.cartagena.tool.core.registry.ApacheRestRegistry

trait RestAgent extends ApacheRestRegistry {

  val restHelper: RestHelper = apacheHttpRestHelper

}
