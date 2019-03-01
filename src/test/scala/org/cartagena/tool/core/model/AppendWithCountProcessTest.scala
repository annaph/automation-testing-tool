package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class AppendWithCountProcessTest extends FlatSpec with Matchers {

  "liftOne ++ count" should "create process to increase only first integer by 3 and then append to count the number " +
    "of processed integers " in {
    // given
    val process = liftOne[Int, Int](_ + 3) ++ count
    val expected = Stream(4, 1, 2)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to increase each integer by 3 and then append to do nothing" in {
    // given
    val process = lift[Int, Int](_ + 3) ++ count
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to filter even integers and then append to do nothing" in {
    // given
    val process = filter[Int](_ % 2 == 0) ++ count
    val expected = Stream(2, 4, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to take first three integers and then append to count the number of processed integers" in {
    // given
    val process = take[Int](3) ++ count
    val expected = Stream(1, 2, 3, 1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to drop first three integers and then append to do nothing" in {
    // given
    val process = drop[Int](3) ++ count
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to take integers while integer is smaller than 4 and then append to count the number " +
    "processed integers" in {
    // given
    val process = takeWhile[Int](_ < 4) ++ count
    val expected = Stream(1, 2, 3, 1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to drop integers while integer is smaller than 4 and then append to do nothing" in {
    // given
    val process = dropWhile[Int](_ < 4) ++ count
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to count the number of processed integers and then append to do nothing" in {
    // given
    val process = count[Int] ++ count
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(11, 12, 13)).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

}
