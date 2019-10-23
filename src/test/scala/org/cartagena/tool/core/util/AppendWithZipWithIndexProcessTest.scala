package org.cartagena.tool.core.util

import org.cartagena.tool.core.util.Process.{lift, liftOne, zipWithIndex}
import org.scalatest.{FlatSpec, Matchers}

class AppendWithZipWithIndexProcessTest extends FlatSpec with Matchers {

  "liftOne ++ zipWithIndex" should "create process to zip only first character with '1' and then append to zip other " +
    "characters with index" in {
    // given
    val process = liftOne[Char, (Char, Int)](_ -> 1) ++ zipWithIndex
    val expected = LazyList('a' -> 1, 'b' -> 0, 'c' -> 1)

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "lift ++ zipWithIndex" should "create process to zip each integer with '0' and then append to do nothing" in {
    // given
    val process = lift[Int, (Int, Int)](i => i -> 0) ++ zipWithIndex
    val expected = LazyList(1 -> 0, 2 -> 0, 3 -> 0)

    // when
    val actual = process(LazyList(1, 2, 3)).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

  "zipWithIndex ++ zipWithIndex" should "create process to zip each character with index and then append to do " +
    "nothing" in {
    // given
    val process = zipWithIndex[Char] ++ zipWithIndex
    val expected = LazyList('a' -> 0, 'b' -> 1, 'c' -> 2)

    // when
    val actual = process(LazyList('a', 'b', 'c')).map(_.get)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}
