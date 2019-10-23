package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Matchers}

class AppendWithDropWhileProcessTest extends FlatSpec with Matchers {

  "liftOne ++ dropWhile" should "create process to increase only first integer by 1 and then append to drop integers " +
    "while integer is smaller than 4" in {
    // given
    val process = liftOne[Int, Int](_ + 1) ++ dropWhile(_ < 4)
    val expected = LazyList(2, 4, 5)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift ++ dropWhile" should "create process to increase each integer by 3 and then append to do nothing" in {
    // given
    val process = lift[Int, Int](_ + 3) ++ dropWhile(_ < 1)
    val expected = LazyList(4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter ++ dropWhile" should "create process to filter even integers and then append to do nothing" in {
    // given
    val process = filter[Int](_ % 2 == 0) ++ dropWhile(_ < 1)
    val expected = LazyList(2, 4, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take ++ dropWhile" should "create process to take first three integers and then append to drop integers while " +
    "integer is smaller than 6" in {
    // given
    val process = take[Int](3) ++ dropWhile(_ < 6)
    val expected = LazyList(1, 2, 3, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop ++ dropWhile" should "create process to drop first three integers and then append to do nothing" in {
    // given
    val process = drop[Int](3) ++ dropWhile(_ < 1)
    val expected = LazyList(4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile ++ dropWhile" should "create process to take integers while integer is smaller than 4 and then append " +
    "to drop integers while integer is smaller than 6" in {
    // given
    val process = takeWhile[Int](_ < 4) ++ dropWhile(_ < 6)
    val expected = LazyList(1, 2, 3, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile ++ dropWhile" should "create process to drop integers while integer is smaller than 4 and then append " +
    "to do nothing" in {
    // given
    val process = dropWhile[Int](_ < 4) ++ dropWhile(_ < 1)
    val expected = LazyList(4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count ++ takeWhile" should "create process to count the number of processed integers and then append to do " +
    "nothing" in {
    // given
    val process = count[Int] ++ dropWhile(_ < 1)
    val expected = LazyList(1, 2, 3)

    // when
    val actual = process(LazyList(11, 12, 13)).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "exists ++ takeWhile" should "create process to find 'true' value and then append to drop values while value is " +
    "'false'" in {
    // given
    val process = exists[Boolean](i => i) ++ dropWhile(i => !i)
    val expected = LazyList(false, false, true, true)

    // when
    val actual = process(LazyList(false, false, true, false, false, true)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
