package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.{Failure, Success}

class FlatMapProcessTest extends FlatSpec with Matchers with Inside {

  case object MyFlatMapException extends Exception

  "flatMap" should "create process that converts processed integers into characters" in {
    // given
    val process = lift[Int, Int](_ + 3).flatMap[String](i => emit(i.toString))
    val expected = Stream("4", "5", "6")

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "flatMap" should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = lift[Int, Int](_ + 3).flatMap[String](i => emit(i.toString))

    // when
    val actual = process(Stream.empty[Int]).map(_.get)

    // then
    actual should be(Stream.empty[String])
  }

  "flatMap" should "create process to result to an empty output stream" in {
    // given
    val process = filter[Int](_ % 2 == 0).flatMap[Int](emit(_))

    // when
    val actual = process(Stream(1, 3, 5)).map(_.get)

    // then
    actual should be(Stream.empty[Int])
  }

  "flatMap" should "create process to handle 'Err' signal happened in original process" in {
    // given
    val process = lift[Int, Int] {
      case 2 => throw MyFlatMapException
      case x => x + 3
    }.flatMap[String](i => emit(i.toString))

    // when
    val actual = process(Stream(1, 2, 3))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success("4") => succeed
      case _ =>
        fail("flatMap should should process integer 1!")
    }

    inside(actual.last) {
      case Failure(MyFlatMapException) => succeed
      case _ =>
        fail("flatMap should handle 'MyFlatMapException'!")
    }
  }

}
