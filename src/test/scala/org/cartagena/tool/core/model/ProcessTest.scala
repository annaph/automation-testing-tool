package org.cartagena.tool.core.model

import java.io.ByteArrayOutputStream

import org.cartagena.tool.core.model.Process._
import org.scalatest.{FlatSpec, Matchers}

class ProcessTest extends FlatSpec with Matchers {

  "lift" should "create process to increase each integer by 3" in {
    val process = lift[Int, Int](_ + 3)
    val actual = process(Stream(1, 2, 3)).map(_.get).toList

    actual should contain inOrderOnly(4, 5, 6)
  }

  "applying process" should "process each integer only once" in {
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

  "filter" should "create process to filter even integers" in {
    val process = filter[Int](_ % 2 == 0)
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get).toList

    actual should contain inOrderOnly(2, 4, 6)
  }

  "take" should "create process to take first three integers" in {
    val process = take[Int](3)
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get).toList

    actual should contain inOrderOnly(1, 2, 3)
  }

  "drop" should "create process to drop first three integers" in {
    val process = drop[Int](3)
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get).toList

    actual should contain inOrderOnly(4, 5, 6)
  }

  "takeWhile" should "create process to take integers while integer is smaller than 4" in {
    val process = takeWhile[Int](_ < 4)
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get).toList

    actual should contain inOrderOnly(1, 2, 3)
  }

  "dropWhile" should "create process to drop integers while integer is smaller than 4" in {
    val process = dropWhile[Int](_ < 4)
    val actual = process(Stream(1, 2, 3, 4, 5, 6)).map(_.get).toList

    actual should contain inOrderOnly(4, 5, 6)
  }

  "count" should "create process to count number of processed characters" in {
    val process = count[Char]
    val actual = process(Stream('a', 'b', 'c')).map(_.get).toList

    actual should contain inOrderOnly(1, 2, 3)
  }

}
