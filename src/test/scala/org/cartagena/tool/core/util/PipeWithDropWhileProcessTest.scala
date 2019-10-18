package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Matchers}

class PipeWithDropWhileProcessTest extends FlatSpec with Matchers {

  "liftOne |> dropWhile" should "create process to increase only first integer by 3 and then drop it" in {
    // given
    val process = liftOne[Int, Int](_ + 3) |> dropWhile(_ < 5)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual shouldBe empty
  }

  "lift |> dropWhile" should "create process to increase each integer by 3 and then drop integers while integer is " +
    "smaller then 6" in {
    // given
    val process = lift[Int, Int](_ + 3) |> dropWhile(_ < 6)
    val expected = Stream(6)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter |> dropWhile" should "create process to filter even integers and then drop integers while integer is " +
    "smaller than 6" in {
    // given
    val process = filter[Int](_ % 2 == 0) |> dropWhile(_ < 6)
    val expected = Stream(6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take |> dropWhile" should "create process to take first three integers and then drop integers while integer is " +
    "smaller than 3" in {
    // given
    val process = take[Int](3) |> dropWhile(_ < 3)
    val expected = Stream(3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop |> dropWhile" should "create process to drop first three integers and then drop integers while integer is " +
    "smaller than 6" in {
    // given
    val process = drop[Int](3) |> dropWhile(_ < 6)
    val expected = Stream(6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile |> dropWhile" should "create process to take integers while integer is smaller than 4 and then drop " +
    "integers while integer is smaller then 3" in {
    // given
    val process = takeWhile[Int](_ < 4) |> dropWhile(_ < 3)
    val expected = Stream(3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile |> dropWhile" should "create process to drop integers while integer is smaller than 4 and then drop " +
    "integers while integer is smaller then 6" in {
    // given
    val process = dropWhile[Int](_ < 4) |> dropWhile(_ < 6)
    val expected = Stream(6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count |> dropWhile" should "create process to count the number of processed characters and then drop counters " +
    "while counter is smaller then 3" in {
    // given
    val process = count[Char] |> dropWhile(_ < 3)
    val expected = Stream(3)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex |> dropWhile" should "create process to zip each character with index and then drop outputs while " +
    "index is less then 2" in {
    // given
    val process = zipWithIndex[Char] |> dropWhile(_._2 < 2)
    val expected = Stream('c' -> 2)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists |> dropWhile" should "create process to find even integer and then drop outputs while output is false" in {
    // given
    val process = exists[Int](_ % 2 == 0) |> dropWhile(o => !o)
    val expected = Stream(true)

    // when
    val actual = process(Stream(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
