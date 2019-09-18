package org.cartagena.tool.core.step

import org.cartagena.tool.core.http.RestHelper
import org.cartagena.tool.core.registry.ApacheRestRegistryTest
import org.cartagena.tool.core.step.StartRestClient.STEP_NAME
import org.mockito.Mockito.{doNothing, verify}
import org.scalatest.{FlatSpec, Matchers}

class StartRestClientTest extends FlatSpec with Matchers with ApacheRestRegistryTest {

  "name" should "return step name" in {
    // given
    val step = new StartRestClient()

    // when
    val actual = step.name

    // then
    actual should be(STEP_NAME)
  }

  "run" should "run Start REST Client step" in {
    // given
    val step = new StartRestClient() {
      override val restHelper: RestHelper = StartRestClientTest.this.apacheHttpRestHelper
    }

    doNothing().when(step.restHelper).startRestClient()

    // when
    step.run()

    // then
    verify(step.restHelper).startRestClient()
  }

}
