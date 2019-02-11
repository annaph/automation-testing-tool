package org.cartagena.tool.core.model

import java.io.ByteArrayOutputStream

import org.cartagena.tool.core.model.Process.lift
import org.scalatest.{FlatSpec, Matchers}

class ProcessTest extends FlatSpec with Matchers {

  "lift" should "create process to increase each integer by 3" in {
    val process = lift[Int, Int](_ + 3)
    val actual = process(Stream(1, 2, 3)).map(_.get).toList

    actual should contain inOrderOnly(4, 5, 6)
  }

  "applying process" should "process each input only once" in {
    val output = new ByteArrayOutputStream()
    Console.withOut(output) {
      val process = lift[Int, Unit] { i =>
        println(s"Number: $i")
      }
      process(Stream(1, 2, 3)).toList
    }

    val outputString = output.toString

    outputString should include regex """Number: 1"""
    outputString should include regex """Number: 2"""
    outputString should include regex """Number: 3"""
  }

}
