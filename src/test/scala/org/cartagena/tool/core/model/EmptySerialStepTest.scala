package org.cartagena.tool.core.model

import org.scalatest.{FlatSpec, Matchers}

class EmptySerialStepTest extends FlatSpec with Matchers {

  "execute" should "execute empty serial setup test with success" in {
    // given
    val serialStep = SerialSetupStep.empty

    // when
    val actual = serialStep.execute()

    // then
    actual should be(PassedStepExecution(serialStep.name))
  }

  "execute" should "execute empty serial test step with success" in {
    // given
    val serialStep = SerialTestStep.empty

    // when
    val actual = serialStep.execute()

    // then
    actual should be(PassedStepExecution(serialStep.name))
  }

  "execute" should "execute empty serial cleanup step with success" in {
    // given
    val serialStep = SerialCleanupStep.empty

    // when
    val actual = serialStep.execute()

    // then
    actual should be(PassedStepExecution(serialStep.name))
  }

}
