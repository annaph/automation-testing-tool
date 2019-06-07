package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Matchers}

class AppendWithTakeWhileProcessTest extends FlatSpec with Matchers {

  "liftOne ++ takeWhile" should "create process to increase only first integer by 1 and then append to take integers " +
    "while integer is smaller than 4" in {
    // given
    val process = liftOne[Int, Int](_ + 1) ++ takeWhile(_ < 4)
    val expected = Stream(2, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift ++ takeWhile" should "create process to increase each integer by 3 and then append to do nothing" in {
    // given
    val process = lift[Int, Int](_ + 3) ++ takeWhile(_ < 10)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter ++ takeWhile" should "create process to filter even integers and then append to do nothing" in {
    // given
    val process = filter[Int](_ % 2 == 0) ++ takeWhile(_ < 10)
    val expected = Stream(2, 4, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take ++ takeWhile" should "create process to take first three integers and then append to take integers while " +
    "integer is smaller than 6" in {
    // given
    val process = take[Int](3) ++ takeWhile(_ < 6)
    val expected = Stream(1, 2, 3, 4, 5)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6, 7)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop ++ takeWhile" should "create process to drop first three integers and then append to do nothing" in {
    // given
    val process = drop[Int](3) ++ takeWhile(_ < 10)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile ++ takeWhile" should "create process to take integers while integer is smaller than 4 and append to " +
    "take integers while integer is smaller than 6" in {
    // given
    val process = takeWhile[Int](_ < 4) ++ takeWhile(_ < 6)
    val expected = Stream(1, 2, 3, 4, 5)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile ++ takeWhile" should "create process to drop integers while integer is smaller than 4 and then append " +
    "to do nothing" in {
    // given
    val process = dropWhile[Int](_ < 4) ++ takeWhile(_ < 10)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count ++ takeWhile" should "create process to count the number of processed integers and then append to do " +
    "nothing" in {
    // given
    val process = count[Int] ++ takeWhile(_ < 10)
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(11, 12, 13)).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "exists ++ takeWhile" should "create process to find 'true' value and then append to take values while value is " +
    "'true'" in {
    // given
    val process = exists[Boolean](i => i) ++ takeWhile(i => i)
    val expected = Stream(false, false, true, true, true)

    // when
    val actual = process(Stream(false, false, true, true, true, false)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
