package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.takeWhile
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.{Failure, Success}

class TakeWhileProcessTest extends FlatSpec with Matchers with Inside {

  case object MyTakeWhileException extends Exception

  "takeWhile" should "create process to take integers while integer is smaller than 4" in {
    // given
    val process = takeWhile[Int](_ < 4)
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream(1, 2, 3, 4, 1, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = takeWhile[Int](_ < 3)

    // when
    val actual = process(Stream.empty).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to take none integer and result to an empty output stream" in {
    // given
    val process = takeWhile[Int](_ < 4)

    // when
    val actual = process(Stream(4, 1, 6)).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to take all integers" in {
    // given
    val process = takeWhile[Int](_ < 10)
    val expected = Stream(1, 2, 3, 4, 1, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 1, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to handle 'Err' signal" in {
    // given
    val process = takeWhile[Int] {
      case 2 => throw MyTakeWhileException
      case x => x < 4
    }

    // when
    val actual = process(Stream(1, 2, 3))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success(1) => succeed
      case _ =>
        fail("takeWhile should emit integer 1!")
    }

    inside(actual.last) {
      case Failure(MyTakeWhileException) => succeed
      case _ =>
        fail("takeWhile should handle 'MyTakeWhileException'!")
    }
  }

  it should "create process to handle 'Kill' signal" in {
    // given
    val process = takeWhile[Int] {
      case 2 => throw Kill
      case x => x < 4
    }
    val expected = Stream(1)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
