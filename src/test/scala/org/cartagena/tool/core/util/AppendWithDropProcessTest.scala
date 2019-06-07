package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Matchers}

class AppendWithDropProcessTest extends FlatSpec with Matchers {

  "liftOne ++ drop" should "create process to increase only first integer by 1 and then append to drop next two " +
    "integers" in {
    // given
    val process = liftOne[Int, Int](_ + 1) ++ drop(2)
    val expected = Stream(2, 4, 5)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift ++ drop" should "create process to increase each integer by 3 and then append to do nothing" in {
    // given
    val process = lift[Int, Int](_ + 3) ++ drop(1)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter ++ drop" should "create process to filter even integers and then append to do nothing" in {
    // given
    val process = filter[Int](_ % 2 == 0) ++ drop(1)
    val expected = Stream(2, 4, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take ++ drop" should "create process to take first three integers and then append to drop next two integers" in {
    // given
    val process = take[Int](3) ++ drop(2)
    val expected = Stream(1, 2, 3, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop ++ drop" should "create process to drop first three integers and then append to do nothing" in {
    // given
    val process = drop[Int](3) ++ drop(1)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile ++ drop" should "create process to take integers while integer is smaller than 4 and then append to " +
    "drop next two integers" in {
    // given
    val process = takeWhile[Int](_ < 4) ++ drop(2)
    val expected = Stream(1, 2, 3, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile ++ drop" should "create process to drop integers while integer is smaller than 4 and then append to do " +
    "nothing" in {
    // given
    val process = dropWhile[Int](_ < 4) ++ drop(1)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count ++ drop" should "create process to count the number of processed integers and then append to do nothing" in {
    // given
    val process = count[Int] ++ drop(1)
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(11, 12, 13)).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "exists ++ drop" should "create process to find 'true' value and then append to drop next two values" in {
    // given
    val process = exists[Boolean](i => i) ++ drop(2)
    val expected = Stream(false, false, true, true)

    // when
    val actual = process(Stream(false, false, true, false, false, true)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
