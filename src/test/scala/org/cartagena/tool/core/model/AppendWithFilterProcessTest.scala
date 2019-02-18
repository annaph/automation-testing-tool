package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class AppendWithFilterProcessTest extends FlatSpec with Matchers {

  "liftOne ++ filter" should "create process to increase only first integer by 1 and then to filter even integers" in {
    // given
    val process = liftOne[Int, Int](_ + 1) ++ filter(_ % 2 == 0)
    val expected = Stream(2, 2, 4)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
