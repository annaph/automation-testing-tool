package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.liftOne
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.Failure

class LiftOneProcessTest extends FlatSpec with Matchers with Inside {

  "liftOne" should "create process to increase only first integer by 3" in {
    // given
    val process = liftOne[Int, Int](_ + 3)
    val expected = LazyList(4)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output lazy list when input lazy list is empty" in {
    // given
    val process = liftOne[Int, Int](_ + 3)

    // when
    val actual = process(LazyList.empty).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to handle 'Err' signal" in {
    // given
    val process = liftOne[String, Int](_.toInt)

    // when
    val actual = process(LazyList("a", "b", "c"))

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
    val actual = process(LazyList("a", "b", "c"))

    // then
    actual shouldBe empty
  }

}
