package org.cartagena.tool.core.model

import java.io.ByteArrayOutputStream

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class ProcessTest extends FlatSpec with Matchers {

  "applying process" should "process each integer only once" in {
    // given
    val process = lift[Int, Unit](i => println(s"Number: $i"))
    val output = new ByteArrayOutputStream()

    // when
    Console.withOut(output) {
      process(Stream(1, 2, 3)).toList
    }
    val outputString = output.toString

    // then
    outputString should include regex """Number: 1"""
    outputString should include regex """Number: 2"""
    outputString should include regex """Number: 3"""
  }

  "drain" should "create process that does not emit any integer" in {
    // given
    val process = lift[Int, Int] { i =>
      println(s"Number: $i")
      i
    }.drain

    var actual = Stream.empty[Int]
    val output = new ByteArrayOutputStream()

    // when
    Console.withOut(output) {
      actual = process(Stream(1, 2, 3)).map(_.get)
    }
    val outputString = output.toString

    // then
    actual should be(Stream.empty[Int])

    outputString should include regex """Number: 1"""
    outputString should include regex """Number: 2"""
    outputString should include regex """Number: 3"""
  }

  "onHalt" should "create process that counts the number of un-taken integers" in {
    // given
    val process = take[Int](3).drain onHalt {
      case End =>
        count[Int]
      case err =>
        halt(err)
    }
    val expected = Stream(1, 2, 3, 4)

    // when
    val actual = process(Stream(11, 12, 13, 14, 15, 16, 17)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
