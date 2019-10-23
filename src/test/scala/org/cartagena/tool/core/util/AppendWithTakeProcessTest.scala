package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Matchers}

class AppendWithTakeProcessTest extends FlatSpec with Matchers {

  "liftOne ++ take" should "create process to increase only first integer by 1 and then append to take next first " +
    "two integers" in {
    // given
    val process = liftOne[Int, Int](_ + 1) ++ take(2)
    val expected = LazyList(2, 2, 3)

    // when
    val actual = process(LazyList(1, 2, 3, 4)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift ++ take" should "create process to increase each integer by 3 and then append to do nothing" in {
    // given
    val process = lift[Int, Int](_ + 3) ++ take(1)
    val expected = LazyList(4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter ++ take" should "create process to filter even integers and then append to do nothing" in {
    // given
    val process = filter[Int](_ % 2 == 0) ++ take(1)
    val expected = LazyList(2, 4, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take ++ take" should "create process to take first three integers and then append to take next first two " +
    "integers" in {
    // given
    val process = take[Int](3) ++ take(2)
    val expected = LazyList(1, 2, 3, 4, 5)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop ++ take" should "create process to drop first three integers and then append to do nothing" in {
    // given
    val process = drop[Int](3) ++ take(2)
    val expected = LazyList(4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile ++ take" should "create process to take integers while integer is smaller than 4 and then append to " +
    "take next first two integers" in {
    // given
    val process = takeWhile[Int](_ < 4) ++ take(2)
    val expected = LazyList(1, 2, 3, 4, 5)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile ++ take" should "create process to drop integers while integer is smaller than 4 and then append to do " +
    "nothing" in {
    // given
    val process = dropWhile[Int](_ < 4) ++ take(1)
    val expected = LazyList(4, 5, 6)

    // when
    val actual = process(LazyList(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count ++ take" should "create process to count the number of processed integers and then append to do nothing" in {
    // given
    val process = count[Int] ++ take(1)
    val expected = LazyList(1, 2, 3)

    // when
    val actual = process(LazyList(11, 12, 13)).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "exists ++ take" should "create process to find 'true' value and then append to take next two values" in {
    // given
    val process = exists[Boolean](i => i) ++ take(1)
    val expected = LazyList(false, false, true, false)

    // when
    val actual = process(LazyList(false, false, true, false, true)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
