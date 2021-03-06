package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Matchers}

class PipeWithDropProcessTest extends FlatSpec with Matchers {

  "liftOne |> drop" should "create process to increase only first integer by 3 and then drop it" in {
    // given
    val process = liftOne[Int, Int](_ + 3) |> drop(1)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual shouldBe empty
  }

  "lift |> drop" should "create process to increase each integer by 3 and then drop first two integers" in {
    // given
    val process = lift[Int, Int](_ + 3) |> drop(2)
    val expected = LazyList(6)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter |> drop" should "create process to filter even integers and then drop first two integers" in {
    // given
    val process = filter[Int](_ % 2 == 0) |> drop(2)
    val expected = LazyList(6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take |> drop" should "create process to take first three integers and then drop first two integers" in {
    // given
    val process = take[Int](3) |> drop(2)
    val expected = LazyList(3)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop |> drop" should "create process to drop first three integers and then drop first two integers" in {
    // given
    val process = drop[Int](3) |> drop(2)
    val expected = LazyList(6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile |> drop" should "create process to take integers while integer is smaller than 4 and then drop first " +
    "two integers" in {
    // given
    val process = takeWhile[Int](_ < 4) |> drop(2)
    val expected = LazyList(3)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile |> drop" should "create process to drop integers while integer is smaller than 4 and then drop first " +
    "two integers" in {
    // given
    val process = dropWhile[Int](_ < 4) |> drop(2)
    val expected = LazyList(6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count |> take" should "create process to count the number of processed characters and then drop first two " +
    "counters" in {
    // given
    val process = count[Char] |> drop(2)
    val expected = LazyList(3)

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex |> drop" should "create process to zip each character with index and then drop first two outputs" in {
    // given
    val process = zipWithIndex[Char] |> drop(2)
    val expected = LazyList('c' -> 2)

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists |> drop" should "create process to find even integer and then drop first two outputs" in {
    // given
    val process = exists[Int](_ % 2 == 0) |> drop(2)
    val expected = LazyList(true)

    // when
    val actual = process(LazyList(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
