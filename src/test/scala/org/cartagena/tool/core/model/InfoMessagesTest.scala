package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepTestUtil.{MyShapelessStep1 => MyStep}
import org.scalatest.{FlatSpec, Matchers}

class InfoMessagesTest extends FlatSpec with Matchers {

  "preRunMsg" should "return pre-run info message" in {
    // given
    val step = MyStep

    // when
    val actual = step.preRunMsg

    // then
    actual should be(s"Executing '${step.name}'...")
  }

  "passedRunMsg" should "return passed run info message" in {
    // given
    val step = MyStep

    // when
    val actual = step.passedRunMsg

    // then
    actual should be(s"Finish executing '${step.name}' with success.")
  }

  "failedRunMsg" should "return failed run info message" in {
    // given
    val step = MyStep

    // when
    val actual = step.failedRunMsg

    // then
    actual should be(s"Finish executing '${step.name}' with failure!")
  }

  "ignoredRunMsg" should "return ignored run info message" in {
    // given
    val step = MyStep

    // when
    val actual = step.ignoredRunMsg

    // then
    actual should be(s"Execution of '${step.name}' ignored!")
  }

}
