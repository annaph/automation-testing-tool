package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.SuiteContextTestUtil.MySuiteContextTest
import org.scalatest.{FlatSpec, Matchers}

class SuiteContextTest extends FlatSpec with Matchers {

  "size" should "return 2 for SuiteContext containing two entries" in {
    // given
    val context = MySuiteContextTest()

    // when
    val actual = context.size

    // then
    actual should be(2)
  }

}
