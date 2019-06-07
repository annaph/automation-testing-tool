package org.cartagena.tool.core.util

import java.io.ByteArrayOutputStream

import org.cartagena.tool.core.util.Process.{filter, lift}
import org.scalatest.{FlatSpec, Inside, Matchers}

import scala.util.Failure

class DrainProcessTest extends FlatSpec with Matchers with Inside {

  case object MyDrainException extends Exception

  "drain" should "create process that does not emit any integer" in {
    // given
    val process = lift[Int, Int] { i =>
      println(s"Number: $i")
      i + 3
    }.drain

    // when
    val output = new ByteArrayOutputStream()
    Console.withOut(output) {
      val actual = process(Stream(1, 2, 3)).map(_.get)

      // then
      actual should be(Stream.empty)
    }

    // then
    val outputString = output.toString

    outputString should include regex """Number: 1"""
    outputString should include regex """Number: 2"""
    outputString should include regex """Number: 3"""
  }

  it should "create process that does not emit any integer when input stream is empty" in {
    // given
    val process = lift[Int, Int] { i =>
      println(s"Number: $i")
      i + 3
    }.drain

    // when
    val output = new ByteArrayOutputStream()
    Console.withOut(output) {
      val actual = process(Stream.empty).map(_.get)

      // then
      actual should be(Stream.empty)
    }

    // then
    output.toString should have length 0
  }

  it should "create process that does not emit any integer when original process results to an empty stream" in {
    // given
    val process = filter[Int] { i =>
      println(s"Number: $i")
      i % 2 == 0
    }.drain

    // when
    val output = new ByteArrayOutputStream()
    Console.withOut(output) {
      val actual = process(Stream(1, 2, 3)).map(_.get)

      // then
      actual should be(Stream.empty)
    }

    // then
    val outputString = output.toString

    outputString should include regex """Number: 1"""
    outputString should include regex """Number: 2"""
    outputString should include regex """Number: 3"""
  }

  it should "create process to handle 'Err' signal" in {
    // given
    val process = lift[Int, Int] {
      case 2 => throw MyDrainException
      case x =>
        println(s"Number: $x")
        x + 3
    }.drain

    // when
    val output = new ByteArrayOutputStream()
    Console.withOut(output) {
      val actual = process(Stream(1, 2, 3))

      // then
      actual should have size 1

      inside(actual.head) {
        case Failure(MyDrainException) => succeed
        case _ =>
          fail("drain should handle 'MyDrainException'!")
      }
    }

    // then
    output.toString should include regex """Number: 1"""
  }

  it should "create process to handle 'Kill' signal" in {
    // given
    val process = lift[Int, Int] {
      case 2 => throw Kill
      case x =>
        println(s"Number: $x")
        x + 3
    }.drain

    // when
    val output = new ByteArrayOutputStream()
    Console.withOut(output) {
      val actual = process(Stream(1, 2, 3)).map(_.get)

      // then
      actual should be(Stream.empty)
    }

    // then
    output.toString should include regex """Number: 1"""
  }

}
