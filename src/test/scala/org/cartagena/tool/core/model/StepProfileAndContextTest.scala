package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepTestUtil.{MyShapelessStep1 => MyStep}
import org.scalatest.{FlatSpec, Matchers}

class StepProfileAndContextTest extends FlatSpec with Matchers {

  "profile" should "return empty profile" in {
    // given
    val step = MyStep

    // when
    val actual = step.profile

    // then
    actual should be(EmptyProfile)
  }

  "context" should "return empty context" in {
    // given
    val step = MyStep

    // when
    val actual = step.context

    // then
    actual should be(EmptyContextX)
  }

}
