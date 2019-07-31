package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepTestUtil.{MyRouterStep, MyRouterStepToFail, MyStepException, MyShapelessStep1 => MyStep, MyShapelessStepToFail1 => MyStepToFail}
import org.scalatest.{FlatSpec, Matchers}

class RouterStepTest extends FlatSpec with Matchers {

  "execute" should "execute Step with success" in {
    // given
    val step = MyRouterStep
    val expectedInnerStepExecutions = PassedStepExecution(MyStep.name) :: Nil

    // /when
    val actual = step.execute()

    // then
    actual should be(PassedStepExecution(step.name, expectedInnerStepExecutions))
  }

  it should "execute Step with failure" in {
    // given
    val step = MyRouterStepToFail
    val expectedInnerStepExecutions = FailedStepExecution(MyStepToFail.name, MyStepException) :: Nil

    // /when
    val actual = step.execute()

    // then
    actual should be(FailedStepExecution(step.name, RouterStepFailed, expectedInnerStepExecutions))
  }

}
