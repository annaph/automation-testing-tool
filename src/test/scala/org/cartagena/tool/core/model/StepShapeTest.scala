package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepTestUtil.MyStepShape
import org.scalatest.{FlatSpec, Matchers}

class StepShapeTest extends FlatSpec with Matchers {

  "ignore" should "ignore executing step" in {
    // given
    val step = MyStepShape

    // when
    val actual = step.ignore

    // then
    actual should be(IgnoredStepExecution(MyStepShape.name))
  }

  "toStream" should "return Stream containing only one step" in {
    // given
    val step = MyStepShape
    val expected = Stream(step)

    // when
    val actual = step.toStream

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
