package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class AppendWithLiftOneProcessTest extends FlatSpec with Matchers {

  "liftOne ++ liftOne" should "create process to increase only first integer by 1 and then append to increase only " +
    "second integer by 2" in {
    // given
    val process = liftOne[Int, Int](_ + 1) ++ liftOne(_ + 2)
    val expected = Stream(2, 4)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift ++ liftOne" should "create process to increase each integer by 3 and then append to do nothing" in {
    // given
    val process = lift[Int, Int](_ + 3) ++ liftOne(_ + 1)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "filter ++ liftOne" should "create process to filter even integers and then append to do nothing" in {
    // given
    val process = filter[Int](_ % 2 == 0) ++ liftOne(_ + 1)
    val expected = Stream(2, 4, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take ++ liftOne" should "create process to take first three integers and then append to increase only fourth " +
    "integer by 3" in {
    // given
    val process = take[Int](3) ++ liftOne(_ + 3)
    val expected = Stream(1, 2, 3, 7)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop ++ liftOne" should "create process to drop first three integers and then append to do nothing" in {
    // given
    val process = drop[Int](3) ++ liftOne(_ + 1)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile ++ liftOne" should "create process to take integers while integer is smaller than 4 and then append to " +
    "increase only fourth integer by 3" in {
    // given
    val process = takeWhile[Int](_ < 4) ++ liftOne(_ + 3)
    val expected = Stream(1, 2, 3, 7)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile ++ liftOne" should "create process to drop integers while integer is smaller than 4 and then append to " +
    "do nothing" in {
    // given
    val process = dropWhile[Int](_ < 4) ++ liftOne(_ + 1)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count ++ liftOne" should "create process to count the number of processed characters and then append to do " +
    "nothing" in {
    // given
    val process = count[Char] ++ liftOne(_ + 1)
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex ++ liftOne" should "create process to zip each character with index and then append to do nothing" in {
    // given
    val process = zipWithIndex[Char] ++ liftOne(_ -> 0)
    val expected = Stream('a' -> 0, 'b' -> 1, 'c' -> 2)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists ++ liftOne" should "create process to find even integer and then append to emit one 'true' value" in {
    // given
    val process = exists[Int](_ % 2 == 0) ++ liftOne(_ => true)
    val expected = Stream(false, false, true, true)

    // when
    val actual = process(Stream(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
