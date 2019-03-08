package org.cartagena.tool.suite.login

object LoginE2ELocalhost extends App {

  profile = onlyLocalHostProfile
  print(s"Suite properties: ${profile.getAllProperties}")

  LoginSuite.run()
}

object LoginE2EVagrant extends App {

  profile = onlyVagrantProfile
  print(s"Suite properties: ${profile.getAllProperties}")

  LoginSuite.run()
}

