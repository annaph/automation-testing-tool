package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.{dropWhile, lift}
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.{Failure, Success}

class PipeProcessTest extends FlatSpec with Matchers with Inside {

  case object MyPipeException extends Exception

  "pipe" should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = lift[Int, Int](_ + 3) |> lift(_.toString)

    // when
    val actual = process(Stream.empty).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to result in empty output stream when 1st process results in empty stream" in {
    // given
    val process = dropWhile[Int](_ => true) |> lift(_.toString)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to result in empty output stream when 2nd process results in empty stream" in {
    // given
    val process = lift[Int, Int](_ + 3) |> dropWhile[Int](_ => true)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to handle 'Err' signal happened in the 1st process" in {
    // given
    val process = lift[Int, Int] {
      case 2 => throw MyPipeException
      case x => x + 3
    } |> lift(_.toString)

    // when
    val actual = process(Stream(1, 2, 3))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success("4") => succeed
      case _ =>
        fail("pipe should emit character '4'!")
    }

    inside(actual.last) {
      case Failure(MyPipeException) => succeed
      case _ =>
        fail("pipe should handle 'MyPipeException'!")
    }
  }

  it should "create process to handle 'Err' signal happened in the 2nd process" in {
    // given
    val process = lift[Int, Int](_ + 3) |> lift {
      case 5 => throw MyPipeException
      case x => x.toString
    }

    // when
    val actual = process(Stream(1, 2, 3))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success("4") => succeed
      case _ =>
        fail("pipe should emit character '4'!")
    }

    inside(actual.last) {
      case Failure(MyPipeException) => succeed
      case _ =>
        fail("pipe should handle 'MyPipeException'!")
    }
  }

  it should "create process to handle 'Kill' signal happened in the 1st process" in {
    // given
    val process = lift[Int, Int] {
      case 2 => throw Kill
      case x => x + 3
    } |> lift(_.toString)
    val expected = Stream("4")

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to handle 'Kill' signal happened in the 2nd process" in {
    // given
    val process = lift[Int, Int](_ + 3) |> lift {
      case 5 => throw Kill
      case x => x.toString
    }
    val expected = Stream("4")

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
