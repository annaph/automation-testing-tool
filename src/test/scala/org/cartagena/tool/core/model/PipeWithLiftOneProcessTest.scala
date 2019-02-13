package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class PipeWithLiftOneProcessTest extends FlatSpec with Matchers {

  "liftOne |> liftOne" should "create process to increase only first integer by 3 and then convert it to character" in {
    // given
    val process = liftOne[Int, Int](_ + 3) |> liftOne(_.toString)
    val expected = Stream("4")

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift |> liftOne" should "create process to increase each integer by 3 and then convert only first to character" in {
    // given
    val process = lift[Int, Int](_ + 3) |> liftOne(_.toString)
    val expected = Stream("4")

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter |> liftOne" should "create process to filter even integers and then convert only first to character" in {
    // given
    val process = filter[Int](_ % 2 == 0) |> liftOne(_.toString)
    val expected = Stream("2")

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take |> liftOne" should "create process to take first three integers and then convert only first to character" in {
    // given
    val process = take[Int](3) |> liftOne(_.toString)
    val expected = Stream("1")

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop |> liftOne" should "create process to drop first three integers and then convert only first to character" in {
    // given
    val process = drop[Int](3) |> liftOne(_.toString)
    val expected = Stream("4")

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile |> liftOne" should "create process to take integers while integer is smaller than 4 and then convert " +
    "only first to character" in {
    // given
    val process = takeWhile[Int](_ < 4) |> liftOne(_.toString)
    val expected = Stream("1")

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile |> liftOne" should "create process to drop integers while integer is smaller than 4 and then convert " +
    "only first to character" in {
    // given
    val process = dropWhile[Int](_ < 4) |> liftOne(_.toString)
    val expected = Stream("4")

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count |> liftOne" should "create process to count number of processed characters and then convert only first " +
    "counter to character" in {
    // given
    val process = count[Char] |> liftOne(_.toString)
    val expected = Stream("1")

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex |> liftOne" should "create process to zip each character with index and then convert only first " +
    "index to character" in {
    // given
    val process = zipWithIndex[Char] |> liftOne(_._2.toString)
    val expected = Stream("0")

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists |> liftOne" should "create process to find even integer and then convert only first output to integer" in {
    // given
    val process = exists[Int](_ % 2 == 0) |> liftOne(if (_) 1 else 0)
    val expected = Stream(0)

    // when
    val actual = process(Stream(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
