package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.zipWithIndex
import org.scalatest.{FlatSpec, Matchers}

class ZipWithIndexProcessTest extends FlatSpec with Matchers {

  "zipWithIndex" should "create process to zip each character with index" in {
    // given
    val process = zipWithIndex[Char]
    val expected = LazyList('a' -> 0, 'b' -> 1, 'c' -> 2)

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output lazy list when input lazy list is empty" in {
    // given
    val process = zipWithIndex[Int]

    // when
    val actual = process(LazyList.empty).map(_.get)

    // then
    actual shouldBe empty
  }

}
