package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.count
import org.scalatest.{FlatSpec, Matchers}

class CountProcessTest extends FlatSpec with Matchers {

  "count" should "create process to count the number of processed characters" in {
    // given
    val process = count[Char]
    val expected = Stream(1, 2, 3)

    // when
    val actual = process(Stream('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output stream when input stream is empty" in {
    // given
    val process = count[Int]

    // when
    val actual = process(Stream.empty).map(_.get)

    // then
    actual should be(Stream.empty)
  }

}
