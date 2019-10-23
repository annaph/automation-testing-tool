package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Matchers}

class PipeWithFilterProcessTest extends FlatSpec with Matchers {

  "liftOne |> filter" should "create process to increase only first integer by 3 and then filter even integers" in {
    // given
    val process = liftOne[Int, Int](_ + 3) |> filter(_ % 2 == 0)
    val expected = LazyList(4)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift |> filter" should "create process to increase each integer by 3 and then filter even integers" in {
    // given
    val process = lift[Int, Int](_ + 3) |> filter(_ % 2 == 0)
    val expected = LazyList(4, 6)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter |> filter" should "create process to filter even integers and then filter integers smaller than 5" in {
    // given
    val process = filter[Int](_ % 2 == 0) |> filter(_ < 5)
    val expected = LazyList(2, 4)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take |> filter" should "create process to take first three integers and then filter odd integers" in {
    // given
    val process = take[Int](3) |> filter(_ % 2 != 0)
    val expected = LazyList(1, 3)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop |> filter" should "create process to drop first three integers and then filter even integers" in {
    // given
    val process = drop[Int](3) |> filter(_ % 2 == 0)
    val expected = LazyList(4, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile |> filter" should "create process to take integers while integer is smaller than 4 and then filter odd " +
    "integers" in {
    // given
    val process = takeWhile[Int](_ < 4) |> filter(_ % 2 != 0)
    val expected = LazyList(1, 3)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile |> filter" should "create process to drop integers while integer is smaller than 4 and then filter even " +
    "integers" in {
    // given
    val process = dropWhile[Int](_ < 4) |> filter(_ % 2 == 0)
    val expected = LazyList(4, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count |> filter" should "create process to count the number of processed characters and then filter odd " +
    "counters" in {
    // given
    val process = count[Char] |> filter(_ % 2 != 0)
    val expected = LazyList(1, 3)

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex |> filter" should "create process to zip each character with index and then filter by even indexes" in {
    // given
    val process = zipWithIndex[Char] |> filter(_._2 % 2 == 0)
    val expected = LazyList('a' -> 0, 'c' -> 2)

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists |> filter" should "create process to find even integer and then filter only true output" in {
    // given
    val process = exists[Int](_ % 2 == 0) |> filter(o => o)
    val expected = LazyList(true)

    // when
    val actual = process(LazyList(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
