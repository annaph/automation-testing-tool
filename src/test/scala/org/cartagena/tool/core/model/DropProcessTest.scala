package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class DropProcessTest extends FlatSpec with Matchers {

  "drop" should "create process to drop first three integers" in {
    // given
    val process = drop[Int](3)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop" should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = drop[Int](3)

    // when
    val actual = process(Stream.empty[Int]).map(_.get)

    // then
    actual should be(Stream.empty[Int])
  }

  "drop" should "create process to drop zero integers" in {
    // given
    val process = drop[Int](0)
    val expected = Stream(1, 2, 3, 4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop" should "create process to drop none integer" in {
    // given
    val process = drop[Int](-1)
    val expected = Stream(1, 2, 3, 4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop" should "create process to drop all integers and result to an empty output stream" in {
    // given
    val process = drop[Int](6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should be(Stream.empty[Int])
  }

  "drop" should "create process to drop more than possible integers and result to an empty output stream" in {
    // given
    val process = drop[Int](12)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should be(Stream.empty[Int])
  }

}
