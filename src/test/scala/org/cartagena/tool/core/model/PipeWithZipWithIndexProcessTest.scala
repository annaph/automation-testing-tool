package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class PipeWithZipWithIndexProcessTest extends FlatSpec with Matchers {

  "liftOne |> zipWithIndex" should "create process to increase only first integer by 3 and then zip it with index" in {
    // given
    val process = liftOne[Int, Int](_ + 3) |> zipWithIndex
    val expected = Stream(4 -> 0)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift |> zipWithIndex" should "create process to increase each integer by 3 and then zip them with index" in {
    // given
    val process = lift[Int, Int](_ + 3) |> zipWithIndex
    val expected = Stream(4 -> 0, 5 -> 1, 6 -> 2)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter |> zipWithIndex" should "create process to filter even integers and then zip them with index" in {
    // given
    val process = filter[Int](_ % 2 == 0) |> zipWithIndex
    val expected = Stream(2 -> 0, 4 -> 1, 6 -> 2)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take |> zipWithIndex" should "create process to take first three integers and then zip them with index" in {
    // given
    val process = take[Int](3) |> zipWithIndex
    val expected = Stream(1 -> 0, 2 -> 1, 3 -> 2)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop |> zipWithIndex" should "create process to drop first three integers and then zip them with index" in {
    // given
    val process = drop[Int](3) |> zipWithIndex
    val expected = Stream(4 -> 0, 5 -> 1, 6 -> 2)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile |> zipWithIndex" should "create process to take integers while integer is smaller than 4 and then zip " +
    "them with index" in {
    // given
    val process = takeWhile[Int](_ < 4) |> zipWithIndex
    val expected = Stream(1 -> 0, 2 -> 1, 3 -> 2)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile |> zipWithIndex" should "create process to drop integers while integer is smaller than 4 and then zip " +
    "them with index" in {
    // given
    val process = dropWhile[Int](_ < 4) |> zipWithIndex
    val expected = Stream(4 -> 0, 5 -> 1, 6 -> 2)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count |> zipWithIndex" should "create process to count the number of processed characters and then zip counters " +
    "with index" in {
    // given
    val process = count[Char] |> zipWithIndex
    val expected = Stream(1 -> 0, 2 -> 1, 3 -> 2)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex |> zipWithIndex" should "create process to zip each character with index and then zip outputs with " +
    "index" in {
    // given
    val process = zipWithIndex[Char] |> zipWithIndex
    val expected = Stream(('a' -> 0) -> 0, ('b' -> 1) -> 1, ('c' -> 2) -> 2)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists |> zipWithIndex" should "create process to find even integer and then zip outputs with index" in {
    // given
    val process = exists[Int](_ % 2 == 0) |> zipWithIndex
    val expected = Stream(false -> 0, false -> 1, true -> 2)

    // when
    val actual = process(Stream(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
