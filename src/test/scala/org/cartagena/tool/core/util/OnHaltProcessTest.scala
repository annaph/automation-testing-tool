package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process._
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.{Failure, Success}

class OnHaltProcessTest extends FlatSpec with Matchers with Inside {

  case object MyOnHaltException extends Exception

  "onHalt" should "create process that counts the number of unprocessed integers when none integer is processed" in {
    // given
    val process = take[Int](0).drain onHalt {
      case End =>
        count[Int]
      case err =>
        halt(err)
    }
    val expected = LazyList(1, 2, 3, 4, 5, 6, 7)

    // when
    val actual = process(LazyList(11, 12, 13, 14, 15, 16, 17)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process that counts the number of unprocessed integers when some integers are processed" in {
    // given
    val process = take[Int](4).drain onHalt {
      case End =>
        count[Int]
      case err =>
        halt(err)
    }
    val expected = LazyList(1, 2, 3)

    // when
    val actual = process(LazyList(11, 12, 13, 14, 15, 16, 17)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process that counts the number of unprocessed integers when all integers are processed" in {
    // given
    val process = takeWhile[Int](_ => true).drain onHalt {
      case End =>
        count[Int]
      case err =>
        halt(err)
    }

    // when
    val actual = process(LazyList(11, 12, 13, 14, 15, 16, 17)).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to result in empty output lazy list when input lazy list is empty" in {
    // given
    val process = take[Int](3) onHalt {
      case End =>
        count[Int]
      case err =>
        halt(err)
    }

    // when
    val actual = process(LazyList.empty).map(_.get)

    // then
    actual shouldBe empty
  }

  it should "create process to handle 'Err' signal happened while processing the first integer" in {
    // given
    val process = lift[Int, Int] {
      case 1 => throw MyOnHaltException
      case x => x + 3
    }.onHalt {
      case MyOnHaltException => count[Int]
      case _ => halt(new Exception)
    }

    // when
    val actual = process(LazyList(1, 2, 3))

    // then
    actual should have size 1

    inside(actual.head) {
      case Failure(MyOnHaltException) => succeed
      case _ =>
        fail("onHalt should handle 'MyOnHaltException'!")
    }
  }

  it should "create process to handle 'Err' signal happened after processing several integers" in {
    // given
    val process = lift[Int, Int] {
      case 2 => throw MyOnHaltException
      case x => x + 3
    }.onHalt {
      case MyOnHaltException => count[Int]
      case _ => halt(new Exception)
    }

    // when
    val actual = process(LazyList(1, 2, 3))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success(4) => succeed
      case _ =>
        fail("onHalt should should process integer 1!")
    }

    inside(actual.last) {
      case Failure(MyOnHaltException) => succeed
      case _ =>
        fail("onHalt should handle 'MyOnHaltException'!")
    }
  }

  it should "create process to handle 'Err' signal happened while processing the last integer" in {
    // given
    val process = lift[Int, Int] {
      case 3 => throw MyOnHaltException
      case x => x + 3
    }.onHalt {
      case MyOnHaltException => count[Int]
      case _ => halt(new Exception)
    }

    // when
    val actual = process(LazyList(1, 2, 3))

    // then
    actual should have size 3

    inside(actual.head) {
      case Success(4) => succeed
      case _ =>
        fail("onHalt should should process integer 1!")
    }

    inside(actual.drop(1).head) {
      case Success(5) => succeed
      case _ =>
        fail("onHalt should should process integer 2!")
    }

    inside(actual.last) {
      case Failure(MyOnHaltException) => succeed
      case _ =>
        fail("onHalt should handle 'MyOnHaltException'!")
    }
  }

  it should "create process to handle 'Kill' signal happened while processing the first integer" in {
    // given
    val process = lift[Int, Int] {
      case 1 => throw Kill
      case x => x + 3
    }.onHalt {
      case Kill => count[Int]
      case _ => halt(new Exception)
    }

    // when
    val actual = process(LazyList(1, 2, 3))

    // then
    actual shouldBe empty
  }

  it should "create process to handle 'Kill' signal happened after processing several integers" in {
    // given
    val process = lift[Int, Int] {
      case 2 => throw Kill
      case x => x + 3
    }.onHalt {
      case Kill => count[Int]
      case _ => halt(new Exception)
    }
    val expected = LazyList(4)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to handle 'Kill' signal happened while processing the last integer" in {
    // given
    val process = lift[Int, Int] {
      case 3 => throw Kill
      case x => x + 3
    }.onHalt {
      case Kill => count[Int]
      case _ => halt(new Exception)
    }
    val expected = LazyList(4, 5)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
