package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.filter
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.{Failure, Success}

class FilterProcessTest extends FlatSpec with Matchers with Inside {

  case object MyFilterException extends Exception

  "filter" should "create process to filter even integers" in {
    // given
    val process = filter[Int](_ % 2 == 0)
    val expected = Stream(2, 4, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to filter all integers" in {
    // given
    val process = filter[Int](_ => true)
    val expected = Stream(1, 2, 3, 4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = filter[Int](_ % 2 == 0)

    // when
    val actual = process(Stream.empty).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to filter even integers and result to an empty output stream" in {
    // given
    val process = filter[Int](_ % 2 == 0)

    // when
    val actual = process(Stream(1, 3, 5)).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to handle 'Err' signal" in {
    // given
    val process = filter[Int] {
      case 3 => throw MyFilterException
      case x if x % 2 == 0 => true
      case _ => false
    }

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success(2) => succeed
      case _ =>
        fail("filter should emit integer 2!")
    }

    inside(actual.last) {
      case Failure(MyFilterException) => succeed
      case _ =>
        fail("filter should handle 'MyFilterException'!")
    }
  }

  it should "create process to handle 'Kill' signal" in {
    // given
    val process = filter[Int] {
      case 5 => throw Kill
      case x if x % 2 == 0 => true
      case _ => false
    }
    val expected = Stream(2, 4)

    // when
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
