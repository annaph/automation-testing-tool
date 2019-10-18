package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepTestUtil.{MyShapelessStep1 => MyStep}
import org.scalatest.{FlatSpec, Matchers}

class StepProfileAndContextTest extends FlatSpec with Matchers {

  "profile" should "return empty Profile" in {
    // given
    val step = MyStep

    // when
    val actual = step.profile

    // then
    actual shouldBe a[EmptyProfile]
  }

  "context" should "return empty Context" in {
    // given
    val step = MyStep

    // when
    val actual = step.context

    // then
    actual.isEmpty should be(true)
  }

}
