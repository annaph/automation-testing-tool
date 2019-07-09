package org.cartagena.tool.core.model

import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.model.StepTestStructures.{MyStepException, MyRouterStep => S2, MyRouterStepToFail => S2ToFail, MyShapelessStep1 => S1, MyShapelessStep2 => S3, MyShapelessStep2ToFail => S3ToFail, MyShapelessStepToFail1 => S1ToFail}
import org.scalatest.{FlatSpec, Matchers}

class SerialStepTest extends FlatSpec with Matchers {

  "execute" should "execute 'S1' with success" in {
    // given
    val serialStep: SerialTestStep = S1
    val expectedInnerStepExecutions = List(PassedStepExecution(S1.name))

    // when
    val actual = serialStep.execute()

    // then
    actual should be(PassedStepExecution(serialStep.name, expectedInnerStepExecutions))
  }

  it should "execute 'S1 + S2 + S3' with success where S2 is a router step" in {
    // given
    val serialStep = S1 + S2 + S3

    val expectedInnerStepExecutions = List(
      PassedStepExecution(S1.name),
      PassedStepExecution(S2.name, List(PassedStepExecution(S1.name))),
      PassedStepExecution(S3.name))

    // when
    val actual = serialStep.execute()

    // then
    actual should be(PassedStepExecution(serialStep.name, expectedInnerStepExecutions))
  }

  it should "execute 'S1ToFail + S2 + S3' with failure where S2 is a router step" in {
    // given
    val serialStep = S1ToFail + S2 + S3

    val expectedInnerStepExecutions = List(
      FailedStepExecution(S1ToFail.name, MyStepException),
      IgnoredStepExecution(S2.name),
      IgnoredStepExecution(S3.name))

    // when
    val actual = serialStep.execute()

    // then
    actual should be(FailedStepExecution(serialStep.name, SerialStepFailed, expectedInnerStepExecutions))
  }

  it should "execute 'S1 + S2ToFail + S3' with failure where S2 is a router step" in {
    // given
    val serialStep = S1 + S2ToFail + S3

    val expectedInnerStepExecutions = List(
      PassedStepExecution(S1.name),
      FailedStepExecution(S2ToFail.name, RouterStepFailed, List(FailedStepExecution(S1ToFail.name, MyStepException))),
      IgnoredStepExecution(S3.name))

    // when
    val actual = serialStep.execute()

    // then
    actual should be(FailedStepExecution(serialStep.name, SerialStepFailed, expectedInnerStepExecutions))
  }

  it should "execute 'S1 + S2 + S3ToFail' with failure where S2 is a router step" in {
    // given
    val serialStep = S1 + S2 + S3ToFail

    val expectedInnerStepExecutions = List(
      PassedStepExecution(S1.name),
      PassedStepExecution(S2.name, List(PassedStepExecution(S1.name))),
      FailedStepExecution(S3ToFail.name, MyStepException))

    // when
    val actual = serialStep.execute()

    // then
    actual should be(FailedStepExecution(serialStep.name, SerialStepFailed, expectedInnerStepExecutions))
  }

}
