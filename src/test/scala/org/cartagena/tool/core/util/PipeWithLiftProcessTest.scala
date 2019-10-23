package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Matchers}

class PipeWithLiftProcessTest extends FlatSpec with Matchers {

  "liftOne |> lift" should "create process to increase only first integer by 3 and then convert it to character" in {
    // given
    val process = liftOne[Int, Int](_ + 3) |> lift(_.toString)
    val expected = LazyList("4")

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift |> lift" should "create process to increase each integer by 3 and then convert them to characters" in {
    // given
    val process = lift[Int, Int](_ + 3) |> lift(_.toString)
    val expected = LazyList("4", "5", "6")

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter |> lift" should "create process to filter even integers and then convert them to characters" in {
    // given
    val process = filter[Int](_ % 2 == 0) |> lift(_.toString)
    val expected = LazyList("2", "4", "6")

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take |> lift" should "create process to take first three integers and then convert them to characters" in {
    // given
    val process = take[Int](3) |> lift(_.toString)
    val expected = LazyList("1", "2", "3")

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop |> lift" should "create process to drop first three integers and then convert rest integers to characters" in {
    // given
    val process = drop[Int](3) |> lift(_.toString)
    val expected = LazyList("4", "5", "6")

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile |> lift" should "create process to take integers while integer is smaller than 4 and then convert them " +
    "to characters" in {
    // given
    val process = takeWhile[Int](_ < 4) |> lift(_.toString)
    val expected = LazyList("1", "2", "3")

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile |> lift" should "create process to drop integers while integer is smaller than 4 and then convert rest " +
    "integers to characters" in {
    // given
    val process = dropWhile[Int](_ < 4) |> lift(_.toString)
    val expected = LazyList("4", "5", "6")

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count |> lift" should "create process to count the number of processed characters and then convert counters to " +
    "characters" in {
    // given
    val process = count[Char] |> lift(_.toString)
    val expected = LazyList("1", "2", "3")

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex |> lift" should "create process to zip each character with index and then convert indexes to " +
    "characters" in {
    // given
    val process = zipWithIndex[Char] |> lift(_._2.toString)
    val expected = LazyList("0", "1", "2")

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists |> lift" should "create process to find even integer and then convert outputs to integers" in {
    // given
    val process = exists[Int](_ % 2 == 0) |> lift(if (_) 1 else 0)
    val expected = LazyList(0, 0, 1)

    // when
    val actual = process(LazyList(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
