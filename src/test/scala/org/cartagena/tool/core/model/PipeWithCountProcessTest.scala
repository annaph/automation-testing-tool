package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class PipeWithCountProcessTest extends FlatSpec with Matchers {

  "liftOne |> count" should "create process to increase only first integer by 3 and then count the number of " +
    "processed integers" in {
    // given
    val process = liftOne[Int, Int](_ + 3) |> count
    val expected = Stream(1)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift |> count" should "create process to increase each integer by 3 and then count the number of processed " +
    "integers" in {
    // given
    val process = lift[Int, Int](_ + 3) |> count
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter |> count" should "create process to filter even integers and then count the number of processed integers" in {
    // given
    val process = filter[Int](_ % 2 == 0) |> count
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take |> count" should "create process to take first three integers and then count the number of processed " +
    "integers" in {
    // given
    val process = take[Int](3) |> count
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop |> count" should "create process to drop first three integers and then count the number of processed " +
    "integers" in {
    // given
    val process = drop[Int](3) |> count
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile |> count" should "create process to take integers while integer is smaller than 4 and then count the " +
    "number of processed integers" in {
    // given
    val process = takeWhile[Int](_ < 4) |> count
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile |> count" should "create process to drop integers while integer is smaller than 4 and then count the " +
    "number of processed integers" in {
    // given
    val process = dropWhile[Int](_ < 4) |> count
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count |> count" should "create process to count the number of processed characters and then count the counters" in {
    // given
    val process = count[Char] |> count
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex |> count" should "create process to zip each character with index and then count the number of " +
    "outputs" in {
    // given
    val process = zipWithIndex[Char] |> count
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists |> count" should "create process to find even integer and then count the number of outputs" in {
    // given
    val process = exists[Int](_ % 2 == 0) |> count
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
