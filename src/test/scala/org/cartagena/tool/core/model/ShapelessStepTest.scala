package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepTestStructures.{MyStepException, MyShapelessStep1 => MyShapelessStep, MyShapelessStepToFail1 => MyShapelessStepToFail}
import org.scalatest.{FlatSpec, Matchers}

class ShapelessStepTest extends FlatSpec with Matchers {

  "execute" should "execute Step with success" in {
    // given
    val step = MyShapelessStep

    // /when
    val actual = step.execute()

    // then
    actual should be(PassedStepExecution(step.name))
  }

  it should "execute Step with failure" in {
    // given
    val step = MyShapelessStepToFail

    // /when
    val actual = step.execute()

    // then
    actual should be(FailedStepExecution(step.name, MyStepException))
  }

}
