package org.cartagena.tool.core.model

import java.io.ByteArrayOutputStream

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class ProcessTest extends FlatSpec with Matchers {

  "liftOne" should "create process to increase only first integer by 3" in {
    // given
    val process = liftOne[Int, Int](_ + 3)
    val expected = Stream(4)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift" should "create process to increase each integer by 3" in {
    // given
    val process = lift[Int, Int](_ + 3)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "applying process" should "process each integer only once" in {
    // given
    val process = lift[Int, Unit](i => println(s"Number: $i"))
    val output = new ByteArrayOutputStream()

    // when
    Console.withOut(output) {
      process(Stream(1, 2, 3)).toList
    }
    val outputString = output.toString

    // then
    outputString should include regex """Number: 1"""
    outputString should include regex """Number: 2"""
    outputString should include regex """Number: 3"""
  }

  "filter" should "create process to filter even integers" in {
    // given
    val process = filter[Int](_ % 2 == 0)
    val expected = Stream(2, 4, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "take" should "create process to take first three integers" in {
    // given
    val process = take[Int](3)
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drop" should "create process to drop first three integers" in {
    // given
    val process = drop[Int](3)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "takeWhile" should "create process to take integers while integer is smaller than 4" in {
    // given
    val process = takeWhile[Int](_ < 4)
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "dropWhile" should "create process to drop integers while integer is smaller than 4" in {
    // given
    val process = dropWhile[Int](_ < 4)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "count" should "create process to count number of processed characters" in {
    // given
    val process = count[Char]
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex" should "create process to zip each character with index" in {
    // given
    val process = zipWithIndex[Char]
    val expected = Stream('a' -> 0, 'b' -> 1, 'c' -> 2)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists" should "create process to find even integer" in {
    // given
    val process = exists[Int](_ % 2 == 0)
    val expected = Stream(false, false, true)

    // when
    val actual = process(Stream(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "exists" should "create process to try to find even integer when it does not exist" in {
    // given
    val process = exists[Int](_ % 2 == 0)
    val expected = Stream(false, false, false)

    // when
    val actual = process(Stream(1, 3, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "map" should "create process that converts processed integers into characters" in {
    // given
    val process = lift[Int, Int](_ + 3) map (_.toString)
    val expected = Stream("4", "5", "6")

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "flatMap" should "create process that converts processed integers into characters" in {
    // given
    val process = lift[Int, Int](identity).flatMap[String](i => emit(i.toString))
    val expected = Stream("1", "2", "3")

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "drain" should "create process that does not emit any integer" in {
    // given
    val process = lift[Int, Int] { i =>
      println(s"Number: $i")
      i
    }.drain

    var actual = Stream.empty[Int]
    val output = new ByteArrayOutputStream()

    // when
    Console.withOut(output) {
      actual = process(Stream(1, 2, 3)).map(_.get)
    }
    val outputString = output.toString

    // then
    actual should be(Stream.empty[Int])

    outputString should include regex """Number: 1"""
    outputString should include regex """Number: 2"""
    outputString should include regex """Number: 3"""
  }

  "onHalt" should "create process that counts number of unprocessed integers" in {
    // given
    val process = take[Int](3).drain onHalt {
      case End =>
        count[Int]
      case err =>
        halt(err)
    }
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(11, 12, 13, 14, 15, 16)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
