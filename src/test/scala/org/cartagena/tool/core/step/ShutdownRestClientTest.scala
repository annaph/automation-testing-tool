package org.cartagena.tool.core.step

import org.cartagena.tool.core.http.RestHelper
import org.cartagena.tool.core.registry.ApacheRestRegistryTest
import org.cartagena.tool.core.step.ShutdownRestClient.STEP_NAME
import org.mockito.Mockito.{doNothing, verify}
import org.scalatest.{FlatSpec, Matchers}

class ShutdownRestClientTest extends FlatSpec with Matchers with ApacheRestRegistryTest {

  "name" should "return step name" in {
    // given
    val step = new ShutdownRestClient()

    // when
    val actual = step.name

    // then
    actual should be(STEP_NAME)
  }

  "run" should "run Shutdown REST Client step" in {
    // given
    val step = new ShutdownRestClient() {
      override val restHelper: RestHelper = ShutdownRestClientTest.this.apacheHttpRestHelper
    }

    doNothing().when(step.restHelper).shutdownRestClient()

    // when
    step.run()

    // then
    verify(step.restHelper).shutdownRestClient()
  }

}
