package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.dropWhile
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.Failure

class DropWhileProcessTest extends FlatSpec with Matchers with Inside {

  case object MyDropWhileException extends Exception

  "dropWhile" should "create process to drop integers while integer is smaller than 4" in {
    // given
    val process = dropWhile[Int](_ < 4)
    val expected = Stream(4, 1, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 1, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = dropWhile[Int](_ < 4)

    // when
    val actual = process(Stream.empty).map(_.get)

    // then
    actual should be(Stream.empty)
  }

  it should "create process to drop none integer" in {
    // given
    val process = dropWhile[Int](_ < 0)
    val expected = Stream(1, 2, 3, 4, 1, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 1, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to drop all integers and result to an empty output stream" in {
    // given
    val process = dropWhile[Int](_ < 4)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should be(Stream.empty)
  }

  it should "create process to handle 'Err' signal" in {
    // given
    val process = dropWhile[Int] {
      case 2 => throw MyDropWhileException
      case x => x < 4
    }

    // when
    val actual = process(Stream(1, 2, 3))

    // then
    actual should have size 1

    inside(actual.head) {
      case Failure(MyDropWhileException) => succeed
      case _ =>
        fail("dropWhile should handle 'MyDropWhileException'!")
    }
  }

  it should "create process to handle 'Kill' signal" in {
    // given
    val process = dropWhile[Int] {
      case 2 => throw Kill
      case x => x < 4
    }

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should be(Stream.empty)
  }

}
