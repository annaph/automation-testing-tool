package org.cartagena.tool.core.registry

import org.cartagena.tool.core.http.RestHelper
import org.cartagena.tool.core.http.apache.{ApacheHttpClient, ApacheHttpClientComponent, ApacheHttpClientImpl, ApacheHttpOperations, ApacheHttpOperationsComponent, ApacheHttpOperationsImpl, ApacheRestHelperComponent, ApacheRestHelperImpl}

trait ApacheRestRegistry
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
