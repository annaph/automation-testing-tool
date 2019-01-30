package org.cartagena.tool.suite.login

import org.cartagena.tool.core._

object Steps {

  class NoopSetupStep(profile: Profile) extends AbstractSetupStep(profile, context) {

    override val name: String = "Login Setup Step"

    override def execute(): Unit =
      println("Nothing to do here...")

    override def nextStep: Option[SetupStep] = None

  }

  class LoginStep(profile: Profile) extends AbstractTestStep(profile, context) {

    override val name: String = "Login Step"

    override def execute(): Unit =
      println("There is work to be done here...")

    override def nextStep: Option[TestStep] = None

  }

  class NoopCleanupSteps(profile: Profile) extends AbstractCleanupStep(profile, context) {

    override val name: String = "Login Cleanup Step"

    override def execute(): Unit =
      println("Nothing to do here...")

    override def nextStep: Option[CleanupStep] = None

  }

}
