package org.cartagena.tool.core.model

import org.scalatest.{FlatSpec, Matchers}

class EndStepTest extends FlatSpec with Matchers {

  "execute" should "execute End step" in {
    // when
    val actual = EndStep.execute()

    // then
    actual should be(PassedStepExecution(EndStep.name))
  }

  "run" should "throw unsupported operation exception" in {
    intercept[UnsupportedOperationException] {
      // when
      EndStep.run()
    }
  }

  "route" should "throw unsupported operation exception" in {
    intercept[UnsupportedOperationException] {
      // when
      EndStep.route()
    }
  }

  "left" should "throw unsupported operation exception" in {
    intercept[UnsupportedOperationException] {
      // when
      EndStep.left
    }
  }

  "right" should "throw unsupported operation exception" in {
    intercept[UnsupportedOperationException] {
      // when
      EndStep.right
    }
  }

}
