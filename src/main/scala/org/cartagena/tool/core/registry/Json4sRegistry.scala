package org.cartagena.tool.core.registry

import org.cartagena.tool.core.http.JsonHelper
import org.cartagena.tool.core.http.json4s._

trait Json4sRegistry extends Json4sClientComponent
  with Json4sOperationsComponent
  with Json4sHelperComponent {

  override private[core] val json4sClient: Json4sClient =
    new Json4sClientImpl

  override private[core] val json4sOperations: Json4sOperations =
    new Json4sOperationsImpl

  override private[core] val json4sHelper: JsonHelper =
    new Json4sHelperImpl(json4sClient, json4sOperations)

}
