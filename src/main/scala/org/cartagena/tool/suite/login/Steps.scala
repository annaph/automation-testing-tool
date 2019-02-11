package org.cartagena.tool.suite.login

import org.cartagena.tool.core.model.{AbstractTestStep, Profile}

object Steps {

  class LoginStep(profile: Profile) extends AbstractTestStep(profile, context) {

    override val name: String = "Login Step 1"

    override def execute(): Unit =
      println("There is work to be done here...")

    override def nextTestStep: LoginStep2 = new LoginStep2(profile)

  }

  class LoginStep2(profile: Profile) extends AbstractTestStep(profile, context) {

    override val name: String = "Login Step 2"

    override def execute(): Unit =
      println("There is work to be done here...")

  }

}
