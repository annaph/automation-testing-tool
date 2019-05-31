package org.cartagena.tool.example.suite.login

object LoginE2ELocalhost extends App {

  loginProfile = onlyLocalHostProfile
  LoginSuite.run()

}

object LoginE2EVagrant extends App {

  loginProfile = onlyVagrantProfile
  LoginSuite.run()

}

