package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class PipeWithTakeProcessTest extends FlatSpec with Matchers {

  "liftOne |> take" should "create process to increase only first integer by 3 and then take it" in {
    // given
    val process = liftOne[Int, Int](_ + 3) |> take(1)
    val expected = Stream(4)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift |> take" should "create process to increase each integer by 3 and then take first two integers" in {
    // given
    val process = lift[Int, Int](_ + 3) |> take(2)
    val expected = Stream(4, 5)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter |> take" should "create process to filter even integers and then take first two integers" in {
    // given
    val process = filter[Int](_ % 2 == 0) |> take(2)
    val expected = Stream(2, 4)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take |> take" should "create process to take first three integers and then take first two integers" in {
    // given
    val process = take[Int](3) |> take(2)
    val expected = Stream(1, 2)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop |> take" should "create process to drop first three integers and then take first two integers" in {
    // given
    val process = drop[Int](3) |> take(2)
    val expected = Stream(4, 5)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile |> take" should "create process to take integers while integer is smaller than 4 and then take first " +
    "two integers" in {
    // given
    val process = takeWhile[Int](_ < 4) |> take(2)
    val expected = Stream(1, 2)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile |> take" should "create process to drop integers while integer is smaller than 4 and then take first " +
    "two integers" in {
    // given
    val process = dropWhile[Int](_ < 4) |> take(2)
    val expected = Stream(4, 5)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count |> take" should "create process to count the number of processed characters and then take first two " +
    "counters" in {
    // given
    val process = count[Char] |> take(2)
    val expected = Stream(1, 2)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex |> take" should "create process to zip each character with index and then take first two outputs" in {
    // given
    val process = zipWithIndex[Char] |> take(2)
    val expected = Stream('a' -> 0, 'b' -> 1)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists |> lift" should "create process to find even integer and then take first two outputs" in {
    // given
    val process = exists[Int](_ % 2 == 0) |> take(2)
    val expected = Stream(false, false)

    // when
    val actual = process(Stream(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
