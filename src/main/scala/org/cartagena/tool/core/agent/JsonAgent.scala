package org.cartagena.tool.core.agent

import org.cartagena.tool.core.http.json4s.Json4sHelper
import org.cartagena.tool.core.http.{JsonHelper, JsonHelperComponent}

trait JsonAgent extends JsonHelperComponent {

  override val jsonHelper: JsonHelper = Json4sHelper()

}
