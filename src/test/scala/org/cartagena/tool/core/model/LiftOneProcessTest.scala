package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process.liftOne
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.Failure

class LiftOneProcessTest extends FlatSpec with Matchers with Inside {

  "liftOne" should "create process to increase only first integer by 3" in {
    // given
    val process = liftOne[Int, Int](_ + 3)
    val expected = Stream(4)

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = liftOne[Int, Int](_ + 3)

    // when
    val actual = process(Stream.empty[Int]).map(_.get)

    // then
    actual should be(Stream.empty[Int])
  }

  it should "create process to handle 'Err' signal" in {
    // given
    val process = liftOne[String, Int](_.toInt)

    // when
    val actual = process(Stream("a", "b", "c"))

    // then
    actual should have size 1

    inside(actual.head) {
      case Failure(_: NumberFormatException) => succeed
      case _ =>
        fail("liftOne should handle 'NumberFormatException'!")
    }
  }

  it should "create process to handle 'Kill' signal" in {
    // given
    val process = liftOne[String, String] {
      case "a" => throw Kill
      case ch => ch
    }

    // when
    val actual = process(Stream("a", "b", "c"))

    // then
    actual should be(Stream.empty[String])
  }

}
