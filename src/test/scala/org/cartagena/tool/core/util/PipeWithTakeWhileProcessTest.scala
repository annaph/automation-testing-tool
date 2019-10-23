package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Matchers}

class PipeWithTakeWhileProcessTest extends FlatSpec with Matchers {

  "liftOne |> takeWhile" should "create process to increase only first integer by 3 and then take it" in {
    // given
    val process = liftOne[Int, Int](_ + 3) |> takeWhile(_ < 5)
    val expected = LazyList(4)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift |> takeWhile" should "create process to increase each integer by 3 and then take integers while integer is " +
    "smaller than 6" in {
    // given
    val process = lift[Int, Int](_ + 3) |> takeWhile(_ < 6)
    val expected = LazyList(4, 5)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter |> takeWhile" should "create process to filter even integers and then take integers while integer is " +
    "smaller than 6" in {
    // given
    val process = filter[Int](_ % 2 == 0) |> takeWhile(_ < 6)
    val expected = LazyList(2, 4)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take |> takeWhile" should "create process to take first three integers and then take integers while integer is " +
    "smaller than 3" in {
    // given
    val process = take[Int](3) |> takeWhile(_ < 3)
    val expected = LazyList(1, 2)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop |> takeWhile" should "create process to drop first three integers and then take integers while integer is " +
    "smaller than 6" in {
    // given
    val process = drop[Int](3) |> takeWhile(_ < 6)
    val expected = LazyList(4, 5)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile |> takeWhile" should "create process to take integers while integer is smaller than 4 and then take " +
    "integers while integer is smaller then 3" in {
    // given
    val process = takeWhile[Int](_ < 4) |> takeWhile(_ < 3)
    val expected = LazyList(1, 2)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile |> takeWhile" should "create process to drop integers while integer is smaller than 4 and then take " +
    "integers while integer is smaller then 6" in {
    // given
    val process = dropWhile[Int](_ < 4) |> takeWhile(_ < 6)
    val expected = LazyList(4, 5)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count |> takeWhile" should "create process to count the number of processed characters and then take counters " +
    "while counter is smaller then 3" in {
    // given
    val process = count[Char] |> takeWhile(_ < 3)
    val expected = LazyList(1, 2)

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex |> takeWhile" should "create process to zip each character with index and then take outputs while " +
    "index is less then 2" in {
    // given
    val process = zipWithIndex[Char] |> takeWhile(_._2 < 2)
    val expected = LazyList('a' -> 0, 'b' -> 1)

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists |> takeWhile" should "create process to find even integer and then take outputs while output is false" in {
    // given
    val process = exists[Int](_ % 2 == 0) |> takeWhile(o => !o)
    val expected = LazyList(false, false)

    // when
    val actual = process(LazyList(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
