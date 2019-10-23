package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.drop
import org.scalatest.{FlatSpec, Matchers}

class DropProcessTest extends FlatSpec with Matchers {

  "drop" should "create process to drop first three integers" in {
    // given
    val process = drop[Int](3)
    val expected = LazyList(4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output lazy list when input lazy list is empty" in {
    // given
    val process = drop[Int](3)

    // when
    val actual = process(LazyList.empty).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to drop zero integers" in {
    // given
    val process = drop[Int](0)
    val expected = LazyList(1, 2, 3, 4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to drop none integer" in {
    // given
    val process = drop[Int](-1)
    val expected = LazyList(1, 2, 3, 4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to drop all integers and result to an empty output lazy list" in {
    // given
    val process = drop[Int](6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to drop more than possible integers and result to an empty output lazy list" in {
    // given
    val process = drop[Int](12)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual shouldBe empty
  }

}
