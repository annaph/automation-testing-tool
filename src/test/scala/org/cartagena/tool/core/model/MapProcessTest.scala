package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.{Failure, Success}

class MapProcessTest extends FlatSpec with Matchers with Inside {

  case object MyMapException extends Exception

  "map" should "create process that converts processed integers into characters" in {
    // given
    val process = lift[Int, Int](_ + 3) map (_.toString)
    val expected = Stream("4", "5", "6")

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "map" should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = lift[Int, Int](_ + 3) map (_.toString)

    // when
    val actual = process(Stream.empty[Int]).map(_.get)

    // then
    actual should be(Stream.empty[String])
  }

  "map" should "create process to result to an empty output stream" in {
    // given
    val process = filter[Int](_ % 2 == 0) map (_.toString)

    // when
    val actual = process(Stream(1, 3, 5)).map(_.get)

    // then
    actual should be(Stream.empty[String])
  }

  "map" should "create process to handle 'Err' signal happened in original process" in {
    // given
    val process = lift[Int, Int] {
      case 2 => throw MyMapException
      case x => x + 3
    }.map(_.toString)

    // when
    val actual = process(Stream(1, 2, 3))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success("4") => succeed
      case _ =>
        fail("map should should process integer 1!")
    }

    inside(actual.last) {
      case Failure(MyMapException) => succeed
      case _ =>
        fail("map should handle 'MyMapException'!")
    }
  }

  "map" should "create process to handle 'Err' signal happened in mapper function" in {
    // given
    val process = lift[Int, Int](_ + 3) map {
      case 5 => throw MyMapException
      case x => x.toString
    }

    // when
    val actual = process(Stream(1, 2, 3))

    // then
    actual should have size 2

    inside(actual.head) {
      case Success("4") => succeed
      case _ =>
        fail("map should should process integer 1!")
    }

    inside(actual.last) {
      case Failure(MyMapException) => succeed
      case _ =>
        fail("map should handle 'MyMapException'!")
    }
  }

  "map" should "create process to handle 'Kill' signal happened in original process" in {
    // given
    // given
    val process = lift[Int, Int] {
      case 2 => throw Kill
      case x => x + 3
    }.map(_.toString)
    val expected = Stream("4")

    // when
    val actual = process(Stream(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "map" should "create process to handle 'Kill' signal happened in mapper function" in {
    // given
    // given
    val process = lift[Int, Int](_ + 3) map {
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
