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
  println("Lift example 2:")
  val ints0: Process[Int, Int] = lift[Int, Int] { x =>
    println("Number: " + x)
    x + 3
  }
  println(ints0(Stream(1, 2, 3, 4, 5, 6)) mkString "")
  println("\t\t\t ===> Should be: (4, 5, 6, 7, 8, 9)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Filter example:")
  val ints = filter[Int](x => x % 2 == 0)(Stream(1, 2, 3, 4))
  println(ints.map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (2, 4)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Take example:")
  val ints2 = take(3)(Stream(1, 2, 3, 4, 5))
  println(ints2.map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (1, 2, 3)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Drop example:")
  val ints3 = drop(2)(Stream(1, 2, 3, 4, 5))
  println(ints3.map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (3, 4, 5)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Take While example:")
  val ints4 = takeWhile[Int](_ < 4)(Stream(1, 2, 3, 4, 5))
  println(ints4.map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (1, 2, ,3)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Drop While example:")
  val ints5 = dropWhile[Int](_ < 3)(Stream(1, 2, 3, 4, 5))
  println(ints5.map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (3, 4, 5)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Count example:")
  val chars = count(Stream("a", "b", "c"))
  println(chars.map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (1, 2, 3)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Pipe example:")
  val ints6 = filter[Int](x => x % 2 == 0) |> lift(_ + 1)
  println(ints6(Stream(1, 2, 3, 4, 5, 6)).map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (3, 5, 7)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Pipe example 2:")
  val ints7: Process[Int, Int] = take(3) |> lift(_ * 3)
  println(ints7(Stream(1, 2, 3, 4, 5, 6)).map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (3, 6, 9)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Append example:")
  val ints8: Process[Int, Int] = take(3) ++ lift(_ + 3)
  print(ints8(Stream(1, 2, 3, 4, 5, 6)).map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (1, 2, 3, 7, 8, 9)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Zip With Index example:")
  val ints9: Process[String, (String, Int)] = zipWithIndex
  print(ints9(Stream("A", "B", "C")).map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (A0, B1, C2)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Exists example:")
  val ints10: Process[Int, Boolean] = exists(_ % 2 == 0)
  print(ints10(Stream(1, 3, 5, 6, 7)).map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (F, F, F, T)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Exists example 2:")
  val ints11: Process[Int, Boolean] = count |> exists(_ > 2)
  print(ints11(Stream(1, 3, 5, 6, 7)).map(_.get) mkString ";")
  println("\t\t\t ===> Should be: (F, F, T)")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Halt(err) example:")
  val ints12: Process[String, Int] = lift(_.toInt)
  print(ints12(Stream("1", "2", "A")) mkString ";")
  println("\t\t\t ===> Should be: (S(1), S(2), F(err))")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Halt(Kill) example:")
  val ints13: Process[Int, Int] = lift {
    case 3 => throw Kill
    case x => x
  }
  print(ints13(Stream(1, 2, 3, 4, 5)) mkString ";")
  println("\t\t\t ===> Should be: (S(1), S(2))")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Append & Halt(err) example:")
  val ints14: Process[Int, Int] = lift[Int, Int] {
    case 3 => throw new Exception
    case x => x
  } ++ lift[Int, Int](_ + 3)
  print(ints14(Stream(1, 2, 3, 4, 5, 6)) mkString ";")
  println("\t\t\t ===> Should be: (S(1), S(2), F(err)) ")
  println("------------------------------------------------------------------------------------------------------")

  println("------------------------------------------------------------------------------------------------------")
  println("Append & Halt(Kill) example:")
  val ints15: Process[Int, Int] = lift[Int, Int] {
    case 3 => throw Kill
    case x => x
  } ++ lift[Int, Int](_ + 3)
  print(ints15(Stream(1, 2, 3, 4, 5, 6)) mkString ";")
  println("\t\t\t ===> Should be: (S(1), S(2))")
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

  println("------------------------------------------------------------------------------------------------------")
  println("Drain example:")
  val ints22: Process[Int, Int] = lift[Int, Int](identity).drain
  print(ints22(Stream(1, 2, 3, 4, 5, 6)) mkString ";")
  println("\t\t\t ===> Should be: empty Stream")
  println("------------------------------------------------------------------------------------------------------")

}
