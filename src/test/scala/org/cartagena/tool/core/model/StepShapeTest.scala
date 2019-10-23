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

  "toLazyList" should "return lazy list containing only one step" in {
    // given
    val step = MyStepShape
    val expected = LazyList(step)

    // when
    val actual = step.toLazyList

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
