package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.take
import org.scalatest.{FlatSpec, Matchers}

class TakeProcessTest extends FlatSpec with Matchers {

  "take" should "create process to take first three integers" in {
    // given
    val process = take[Int](3)
    val expected = LazyList(1, 2, 3)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output lazy list when input lazy list is empty" in {
    // given
    val process = take[Int](3)

    // when
    val actual = process(LazyList.empty).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to take zero integers and result to an empty output lazy list" in {
    // given
    val process = take[Int](0)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual shouldBe empty
  }

  "take" should "create process to take none integer and result to an empty output lazy list" in {
    // given
    val process = take[Int](-1)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to take all integers" in {
    // given
    val process = take[Int](6)
    val expected = LazyList(1, 2, 3, 4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to take more integers than possible" in {
    // given
    val process = take[Int](12)
    val expected = LazyList(1, 2, 3, 4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
