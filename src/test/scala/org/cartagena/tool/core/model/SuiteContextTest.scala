package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.SuiteContextTestUtil.{SuiteContextTest => MyContext}
import org.scalatest.{FlatSpec, Matchers}

class SuiteContextTest extends FlatSpec with Matchers {

  "size" should "return 2 for SuiteContext containing two entries" in {
    // given
    val context = MyContext()

    // when
    val actual = context.size

    // then
    actual should be(2)
  }

}
