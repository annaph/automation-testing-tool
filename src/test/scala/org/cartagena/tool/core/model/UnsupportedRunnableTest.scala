package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepTestUtil.MyUnsupportedRunnableStep
import org.scalatest.{FlatSpec, Matchers}

class UnsupportedRunnableTest extends FlatSpec with Matchers {

  "run" should "throw unsupported operation exception" in {
    intercept[UnsupportedOperationException] {
      // when
      MyUnsupportedRunnableStep.run()
    }
  }

}
