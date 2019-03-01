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

}
