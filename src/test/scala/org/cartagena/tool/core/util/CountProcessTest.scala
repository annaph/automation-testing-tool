package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.count
import org.scalatest.{FlatSpec, Matchers}

class CountProcessTest extends FlatSpec with Matchers {

  "count" should "create process to count the number of processed characters" in {
    // given
    val process = count[Char]
    val expected = LazyList(1, 2, 3)

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    actual should contain theSameElementsInOrderAs expected
  }

  it should "create process to result in empty output lazy list when input lazy list is empty" in {
    // given
    val process = count[Int]

    // when
    val actual = process(LazyList.empty).map(_.get)

    // then
    actual shouldBe empty
  }

}
