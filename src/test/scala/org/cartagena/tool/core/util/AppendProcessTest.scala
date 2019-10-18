package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.{dropWhile, halt, lift}
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.{Failure, Success}

class AppendProcessTest extends FlatSpec with Matchers with Inside {

  case object MyAppendException extends Exception

  "append" should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = lift[Int, Int](_ + 3) ++ lift(_ + 1)

    // when
    val actual = process(Stream.empty).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process where all integers are processed only by the 1st process" in {
    // given
    val process = lift[Int, Int](_ + 3) ++ dropWhile[Int](_ => true)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process where all integers are processed only by the 2nd process" in {
    // given
    val process = halt[Int, Int] ++ lift(_ + 3)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output stream when 1st process results in empty stream" in {
    // given
    val process = dropWhile[Int](_ => true) ++ lift(_ + 1)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to result in empty output stream when 1st process halts immediately and 2nd process " +
    "results in empty stream" in {
    // given
    val process = halt[Int, Int] ++ dropWhile[Int](_ => true)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to handle 'Err' signal happened in the 1st process" in {
    // given
    val process = lift[Int, Int] {
      case 2 => throw MyAppendException
      case x => x + 3
    } ++ lift(_ + 1)

    // when
    val actual = process(Stream(1, 2, 3))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success(4) => succeed
      case _ =>
        fail("append should emit integer 4!")
    }

    inside(actual.last) {
      case Failure(MyAppendException) => succeed
      case _ =>
        fail("append should handle 'MyAppendException'!")
    }
  }

  it should "create process to handle 'Err' signal happened in the 2nd process" in {
    // given
    val process = halt[Int, Int] ++ lift {
      case 2 => throw MyAppendException
      case x => x + 3
    }

    // when
    val actual = process(Stream(1, 2, 3))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success(4) => succeed
      case _ =>
        fail("append should emit integer 4!")
    }

    inside(actual.last) {
      case Failure(MyAppendException) => succeed
      case _ =>
        fail("append should handle 'MyAppendException'!")
    }
  }

  it should "create process to handle 'Kill' signal happened in the 1st process" in {
    // given
    val process = lift[Int, Int] {
      case 2 => throw Kill
      case x => x + 3
    } ++ lift(_ + 1)
    val expected = Stream(4)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to handle 'Kill' signal happened in the 2nd process" in {
    // given
    val process = halt[Int, Int] ++ lift[Int, Int] {
      case 2 => throw Kill
      case x => x + 3
    }
    val expected = Stream(4)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
