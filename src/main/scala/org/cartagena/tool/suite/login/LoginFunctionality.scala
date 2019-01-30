package org.cartagena.tool.suite.login

import org.cartagena.tool.core._

object LoginFunctionalityAndLocalHostProfile extends App {

  val suite = LoginTestCase(onlyLocalHostProfile)

  val props = suite.profile.getAllProperties
  print(props)

  val steps = suite.getSteps.map(_.name).mkString("\n")
  println("\n" + steps)

}
