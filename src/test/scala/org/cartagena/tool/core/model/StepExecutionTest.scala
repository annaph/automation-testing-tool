package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepExecutionTest._
import org.cartagena.tool.core.model.StepTestUtil.MyStepException
import org.scalatest.{FlatSpec, Matchers}

class StepExecutionTest extends FlatSpec with Matchers {

  "toStepReport" should "convert passed step execution to step report with Passed status" in {
    // given
    val stepExecution = PassedStepExecution(STEP_NAME)

    // when
    val actual = stepExecution.toStepReport

    // then
    actual should be(StepReport(STEP_NAME, Passed))
  }

  it should "convert failed step execution to step report with Failed status" in {
    // given
    val stepExecution = FailedStepExecution(STEP_NAME, MyStepException)

    // when
    val actual = stepExecution.toStepReport

    // then
    actual should be(StepReport(STEP_NAME, Failed))
  }

  it should "convert ignored step execution to step report with ignored status" in {
    // given
    val stepExecution = IgnoredStepExecution(STEP_NAME)

    // when
    val actual = stepExecution.toStepReport

    // then
    actual should be(StepReport(STEP_NAME, Ignored))
  }

  "leafs" should "return step execution leafs of a step execution without children" in {
    // given
    val stepExecution = PassedStepExecution(STEP_NAME)

    // when
    val actual = stepExecution.leafs

    // then
    actual should be(List(stepExecution))
  }

  it should "return step execution leafs of a step execution with one child" in {
    // given
    val childrenStepExecutions = List(
      PassedStepExecution(LEAF_STEP_NAME_1))

    val stepExecution = PassedStepExecution(STEP_NAME, childrenStepExecutions)

    // when
    val actual = stepExecution.leafs

    // then
    actual should contain theSameElementsInOrderAs childrenStepExecutions
  }

  it should "return step execution leafs of a step execution with three children" in {
    // given
    val childrenStepExecutions = List(
      PassedStepExecution(LEAF_STEP_NAME_1),
      PassedStepExecution(LEAF_STEP_NAME_2),
      PassedStepExecution(LEAF_STEP_NAME_3))

    val stepExecution = PassedStepExecution(STEP_NAME, childrenStepExecutions)

    // when
    val actual = stepExecution.leafs

    // then
    actual should contain theSameElementsInOrderAs childrenStepExecutions
  }

  it should "return step execution leafs of a step execution with complex hierarchy" in {
    // given
    val stepExecution = PassedStepExecution(STEP_NAME,
      List(
        PassedStepExecution(LEAF_STEP_NAME_1,
          List(
            PassedStepExecution(LEAF_STEP_NAME_4),
            PassedStepExecution(LEAF_STEP_NAME_5),
            PassedStepExecution(LEAF_STEP_NAME_6))),
        PassedStepExecution(LEAF_STEP_NAME_2,
          List(
            PassedStepExecution(LEAF_STEP_NAME_7,
              List(
                PassedStepExecution(LEAF_STEP_NAME_8),
                PassedStepExecution(LEAF_STEP_NAME_9),
                PassedStepExecution(LEAF_STEP_NAME_10))))),
        PassedStepExecution(LEAF_STEP_NAME_3)))

    val expected = List(
      PassedStepExecution(LEAF_STEP_NAME_4),
      PassedStepExecution(LEAF_STEP_NAME_5),
      PassedStepExecution(LEAF_STEP_NAME_6),
      PassedStepExecution(LEAF_STEP_NAME_8),
      PassedStepExecution(LEAF_STEP_NAME_9),
      PassedStepExecution(LEAF_STEP_NAME_10),
      PassedStepExecution(LEAF_STEP_NAME_3))

    // when
    val actual = stepExecution.leafs

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}

object StepExecutionTest {

  val STEP_NAME = "stepName"

  val LEAF_STEP_NAME_1 = "leafStepName1"

  val LEAF_STEP_NAME_2 = "leafStepName2"

  val LEAF_STEP_NAME_3 = "leafStepName3"

  val LEAF_STEP_NAME_4 = "leafStepName4"

  val LEAF_STEP_NAME_5 = "leafStepName5"

  val LEAF_STEP_NAME_6 = "leafStepName6"

  val LEAF_STEP_NAME_7 = "leafStepName7"

  val LEAF_STEP_NAME_8 = "leafStepName8"

  val LEAF_STEP_NAME_9 = "leafStepName9"

  val LEAF_STEP_NAME_10 = "leafStepName10"

}
