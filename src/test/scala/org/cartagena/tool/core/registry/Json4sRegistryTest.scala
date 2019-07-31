package org.cartagena.tool.core.registry

import org.cartagena.tool.core.http.JsonHelper
import org.cartagena.tool.core.http.json4s._
import org.scalatest.mockito.MockitoSugar

trait Json4sRegistryTest
  extends Json4sClientComponent
    with Json4sOperationsComponent
    with Json4sHelperComponent
    with MockitoSugar {

  override private[core] val json4sClient = mock[Json4sClient]

  override private[core] val json4sOperations = mock[Json4sOperations]

  override private[core] val json4sHelper = mock[JsonHelper]

}
