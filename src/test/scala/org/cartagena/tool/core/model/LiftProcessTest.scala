package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.{Failure, Success}

class LiftProcessTest extends FlatSpec with Matchers with Inside {

  "lift" should "create process to increase each integer by 3" in {
    // given
    val process = lift[Int, Int](_ + 3)
    val expected = Stream(4, 5, 6)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift" should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = lift[Int, Int](_ + 3)

    // when
    val actual = process(Stream.empty[Int]).map(_.get)

    // then
    actual should be(Stream.empty[Int])
  }

  "lift" should "create process to handle 'Err' signal" in {
    // given
    val process = lift[String, Int](_.toInt)

    // when
    val actual = process(Stream("1", "b", "3"))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success(1) => succeed
      case _ =>
        fail("lift should emit character '1'!")
    }

    inside(actual.last) {
      case Failure(_: NumberFormatException) => succeed
      case _ =>
        fail("lift should handle 'NumberFormatException'!")
    }
  }

  "lift" should "create process to handle 'Kill' signal" in {
    // given
    val process = lift[String, String] {
      case "b" => throw Kill
      case ch => ch
    }
    val expected = Stream("a")

    // when
    val actual = process(Stream("a", "b", "c")).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
