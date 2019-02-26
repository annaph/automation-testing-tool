package org.cartagena.tool.suite.login

import org.cartagena.tool.core.model.Process._
import org.cartagena.tool.core.model._

object LoginFunctionalityAndLocalHostProfile extends App {

  val testCase = LoginTestCase(onlyLocalHostProfile)

  val props = testCase.profile.getAllProperties
  print(props)

  val steps = testCase.testSteps.map(_.name).mkString("\n")
  println("\n" + steps)

  val f: Step => Step = step => {
    println(s"Step to execute: ${step.name}")
    step.execute()

    step
  }

  println("------------------------------------------------------------------------------------------------------")
  println("Lift example:")
  val stepsProcess: Process[Step, Step] = lift[Step, Step](f)
  println(stepsProcess(testCase.testSteps) mkString "")
  println("\t\t\t ===> Should be: ('LoginStep1', 'LoginStep2')")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Flat Map + Halt(err) example:")
  val ints16: Process[Int, Int] = lift[Int, Int] {
    case 3 => throw new Exception
    case x => x
  }.flatMap(n => Emit(n))
  print(ints16(Stream(1, 2, 3, 4, 5, 6)) mkString ";")
  println("\t\t\t ===> Should be: (S(1), S(2), F(err))")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Flat Map + Halt(Kill) example:")
  val ints17: Process[Int, Int] = lift[Int, Int] {
    case 3 => throw Kill
    case x => x
  }.flatMap(n => Emit(n))
  print(ints17(Stream(1, 2, 3, 4, 5, 6)) mkString ";")
  println("\t\t\t ===> Should be: (S(1), S(2))")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Append + Halt(err) on 1st process example:")
  val ints18: Process[String, Int] = lift[String, Int](_.toInt) |> lift(_ + 3)
  print(ints18(Stream("1", "2", "A", "4", "5", "6")) mkString ";")
  println("\t\t\t ===> Should be: (S(4), S(5), F(err))")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Append + Halt(err) on 2nd process example:")
  val ints19: Process[String, Int] = lift[String, Int](_.toInt) |> lift {
    case 4 => throw new Exception
    case x => x + 3
  }
  print(ints19(Stream("1", "2", "3", "4", "5", "6")) mkString ";")
  println("\t\t\t ===> Should be: (S(4), S(5), S(6), F(err))")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Append + Halt(Kill) on 1st process example:")
  val ints20: Process[String, Int] = lift[String, Int] {
    case "3" => throw Kill
    case x => x.toInt
  } |> lift(_ + 3)
  print(ints20(Stream("1", "2", "3", "4", "5", "6")) mkString ";")
  println("\t\t\t ===> Should be: (S(4), S(5))")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Append + Halt(Kill) on 2nd process example:")
  val ints21: Process[String, Int] = lift[String, Int](_.toInt) |> lift {
    case 3 => throw Kill
    case x => x + 3
  }
  print(ints21(Stream("1", "2", "3", "4", "5", "6")) mkString ";")
  println("\t\t\t ===> Should be: (S(4), S(5))")
  println("------------------------------------------------------------------------------------------------------")

}
