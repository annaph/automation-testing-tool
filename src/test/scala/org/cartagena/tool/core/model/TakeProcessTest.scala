package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class TakeProcessTest extends FlatSpec with Matchers {

  "take" should "create process to take first three integers" in {
    // given
    val process = take[Int](3)
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take" should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = take[Int](3)

    // when
    val actual = process(Stream.empty[Int]).map(_.get)

    // then
    actual should be(Stream.empty[Int])
  }

  "take" should "create process to take zero integers and result to an empty output stream" in {
    // given
    val process = take[Int](0)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should be(Stream.empty[Int])
  }

  "take" should "create process to take none integer and result to an empty output stream" in {
    // given
    val process = take[Int](-1)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should be(Stream.empty[Int])
  }

  "take" should "create process to take all integers" in {
    // given
    val process = take[Int](6)
    val expected = Stream(1, 2, 3, 4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take" should "create process to take more integers than possible" in {
    // given
    val process = take[Int](12)
    val expected = Stream(1, 2, 3, 4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
