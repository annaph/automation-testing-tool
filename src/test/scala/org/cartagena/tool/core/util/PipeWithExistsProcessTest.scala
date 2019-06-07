package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Matchers}

class PipeWithExistsProcessTest extends FlatSpec with Matchers {

  "liftOne |> exists" should "create process to increase only first integer by 3 and then find even integer" in {
    // given
    val process = liftOne[Int, Int](_ + 3) |> exists(_ % 2 == 0)
    val expected = Stream(true)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift |> exists" should "create process to increase each integer by 3 and then find odd integer" in {
    // given
    val process = lift[Int, Int](_ + 3) |> exists(_ % 2 != 0)
    val expected = Stream(false, true)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter |> exists" should "create process to filter even integers and then find integer equal to 6" in {
    // given
    val process = filter[Int](_ % 2 == 0) |> exists(_ == 6)
    val expected = Stream(false, false, true)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take |> exists" should "create process to take first three integers and then find even integer" in {
    // given
    val process = take[Int](3) |> exists(_ % 2 == 0)
    val expected = Stream(false, true)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop |> exists" should "create process to drop first three integers and then find odd integer" in {
    // given
    val process = drop[Int](3) |> exists(_ % 2 != 0)
    val expected = Stream(false, true)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile |> exists" should "create process to take integers while integer is smaller than 4 and then find even " +
    "integer" in {
    // given
    val process = takeWhile[Int](_ < 4) |> exists(_ % 2 == 0)
    val expected = Stream(false, true)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile |> exists" should "create process to drop integers while integer is smaller than 4 and then find odd " +
    "integer " in {
    // given
    val process = dropWhile[Int](_ < 4) |> exists(_ % 2 != 0)
    val expected = Stream(false, true)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count |> exists" should "create process to count the number of processed characters and then find counter equal " +
    "to 3" in {
    // given
    val process = count[Char] |> exists(_ == 3)
    val expected = Stream(false, false, true)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex |> exists" should "create process to zip each character with index and then find index equal to 2" in {
    // given
    val process = zipWithIndex[Char] |> exists(_._2 == 2)
    val expected = Stream(false, false, true)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists |> exists" should "create process to find even integer and then find output equal to true" in {
    // given
    val process = exists[Int](_ % 2 == 0) |> exists(o => o)
    val expected = Stream(false, false, true)

    // when
    val actual = process(Stream(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
