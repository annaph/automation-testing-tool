package org.cartagena.tool.core.model

import org.scalatest.{FlatSpec, Matchers}

class StepExecutionTest extends FlatSpec with Matchers {

  val STEP_NAME = "step"

  ignore should "convert passed step execution to step report with Passed status" in {
    // given

    // when

    // then

    ???
  }

  ignore should "convert failed step execution to step report with Failed status" in {
    // given

    // when

    // then

    ???
  }

  ignore should "convert ignored step execution to step report with ignored status" in {
    // given

    // when

    // then

    ???
  }

  ignore should "" in {
    // given
    val stepExecution = PassedStepExecution(STEP_NAME)
    val expected = Stream(StepReport(STEP_NAME, Passed))

    // when
    val actual = stepExecution.toInnerStepReports

    // then
    actual should contain theSameElementsInOrderAs expected

    ???
  }

}
