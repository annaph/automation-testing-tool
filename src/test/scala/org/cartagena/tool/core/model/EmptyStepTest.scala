package org.cartagena.tool.core.model

import org.scalatest.{FlatSpec, Matchers}

class EmptyStepTest extends FlatSpec with Matchers {

  "execute" should "execute Empty step with success" in {
    // when
    val actual = EmptyStep.execute()

    // then
    actual should be(PassedStepExecution(EmptyStep.name))
  }

  "ignore" should "ignore executing Empty step" in {
    // when
    val actual = EmptyStep.ignore

    // then
    actual should be(IgnoredStepExecution(EmptyStep.name))
  }

  "toLazyList" should "return empty" in {
    // when
    val actual = EmptyStep.toLazyList

    // then
    actual shouldBe empty
  }

  "route" should "throw unsupported operation exception" in {
    intercept[UnsupportedOperationException] {
      // when
      EmptyStep.route()
    }
  }

  "left" should "throw unsupported operation exception" in {
    intercept[UnsupportedOperationException] {
      // when
      EmptyStep.left
    }
  }

  "right" should "throw unsupported operation exception" in {
    intercept[UnsupportedOperationException] {
      // when
      EmptyStep.right
    }
  }

}
