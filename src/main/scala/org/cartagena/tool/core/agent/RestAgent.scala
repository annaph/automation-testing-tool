package org.cartagena.tool.core.agent

import org.cartagena.tool.core.http.RestHelper
import org.cartagena.tool.core.http.apache.{ApacheHttpClient, ApacheHttpClientComponent, ApacheHttpClientImpl, ApacheHttpOperations, ApacheHttpOperationsComponent, ApacheHttpOperationsImpl, ApacheRestHelperComponent, ApacheRestHelperImpl}

trait ApacheRestAgent
  extends ApacheHttpClientComponent
    with ApacheHttpOperationsComponent
    with ApacheRestHelperComponent {

  override private[core] val apacheHttpClient: ApacheHttpClient =
    new ApacheHttpClientImpl

  override private[core] val apacheHttpOperations: ApacheHttpOperations =
    new ApacheHttpOperationsImpl

  override private[core] val apacheHttpRestHelper: RestHelper =
    new ApacheRestHelperImpl(apacheHttpClient, apacheHttpOperations)

}

trait RestAgent extends ApacheRestAgent {

  val restHelper: RestHelper = apacheHttpRestHelper

}
