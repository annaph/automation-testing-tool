package org.cartagena.tool.example.suite.login

import org.cartagena.tool.core.CartagenaUtils._

object LoginE2ELocalhost extends App {

  loginProfile = onlyLocalHostProfile

  LoginSuite.run().print()

}

object LoginE2EVagrant extends App {

  loginProfile = onlyVagrantProfile

  LoginSuite.run().print()

}
