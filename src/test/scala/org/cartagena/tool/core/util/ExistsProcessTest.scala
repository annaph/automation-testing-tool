package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.exists
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.{Failure, Success}

class ExistsProcessTest extends FlatSpec with Matchers with Inside {

  case object MyExistsException extends Exception

  "exists" should "create process to find even integer" in {
    // given
    val process = exists[Int](_ % 2 == 0)
    val expected = Stream(false, false, true)

    // when
    val actual = process(Stream(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = exists[Int](_ % 2 == 0)

    // when
    val actual = process(Stream.empty).map(_.get)

    // then
    actual should be(Stream.empty)
  }

  it should "create process to try to find even integer when it does not exist" in {
    // given
    val process = exists[Int](_ % 2 == 0)
    val expected = Stream(false, false, false)

    // when
    val actual = process(Stream(1, 3, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to handle 'Err' signal" in {
    // given
    val process = exists[Int] {
      case 3 => throw MyExistsException
      case x => x % 2 == 0
    }

    // when
    val actual = process(Stream(1, 3, 4, 5))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success(false) => succeed
      case _ =>
        fail("exists should emit 'false'!")
    }

    inside(actual.last) {
      case Failure(MyExistsException) => succeed
      case _ =>
        fail("exists should handle 'MyExistsException'!")
    }
  }

  it should "create process to handle 'Kill' signal" in {
    // given
    val process = exists[Int] {
      case 3 => throw Kill
      case x => x % 2 == 0
    }
    val expected = Stream(false)

    // when
    val actual = process(Stream(1, 3, 4, 5)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
