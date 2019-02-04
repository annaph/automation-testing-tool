package org.cartagena.tool.suite.login

import org.cartagena.tool.core._
import org.cartagena.tool.core.Process._

object LoginFunctionalityAndLocalHostProfile extends App {

  val suite = LoginTestCase(onlyLocalHostProfile)

  val props = suite.profile.getAllProperties
  print(props)

  val steps = suite.getSteps.map(_.name).mkString("\n")
  println("\n" + steps)

  val f: Step => Step = step => {
    println("-------------------------")
    println(step.name)
    step.execute()
    println("-------------------------")

    step
  }

  println("Lift example:")
  val l = lift(f)(suite.getSteps).toList.map(_.name)
  println(l mkString "; ")

  println("Filter example:")
  val ints = filter[Int](x => x % 2 == 0)(Stream(1, 2, 3, 4))
  println(ints mkString ";")

  println("Take example:")
  val ints2 = take(3)(Stream(1, 2, 3, 4, 5))
  println(ints2 mkString ";")

  println("Drop example:")
  val ints3 = drop(2)(Stream(1, 2, 3, 4, 5))
  println(ints3 mkString ";")

  println("Take While example:")
  val ints4 = takeWhile[Int](_ < 4)(Stream(1, 2, 3, 4, 5))
  println(ints4 mkString ";")

  println("Drop While example:")
  val ints5 = dropWhile[Int](_ < 3)(Stream(1, 2, 3, 4, 5))
  println(ints5 mkString ";")

  println("Count example:")
  val chars = count(Stream("a", "b", "c"))
  println(chars mkString ";")

  println("Pipe example:")
  val ints6 = filter[Int](x => x % 2 == 0) |> lift(_ + 1)
  println(ints6(Stream(1, 2, 3, 4, 5, 6)) mkString ";")

  println("Pipe example 2:")
  val ints7: Process[Int, Int] = take(3) |> lift(_ * 3)
  println(ints7(Stream(1, 2, 3, 4, 5, 6)) mkString ";")

}
