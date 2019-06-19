package org.cartagena.tool.example.suite.login

object LoginE2ELocalhost extends App {

  loginProfile = onlyLocalHostProfile

  val suiteReport = LoginSuite.run()

  println(suiteReport)

}

object LoginE2EVagrant extends App {

  loginProfile = onlyVagrantProfile

  val suiteReport = LoginSuite.run()

  println(suiteReport)

}
