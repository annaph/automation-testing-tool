package org.cartagena.tool.suite.login

object LoginE2ELocalhost extends App {

  profile = onlyLocalHostProfile
  LoginSuite.run()

}

object LoginE2EVagrant extends App {

  profile = onlyVagrantProfile
  LoginSuite.run()

}

