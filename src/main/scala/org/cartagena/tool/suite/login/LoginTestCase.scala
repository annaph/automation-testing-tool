package org.cartagena.tool.suite.login

import org.cartagena.tool.core._
import Steps._
import org.cartagena.tool.suite.login

case class LoginTestCase(profile: Profile) extends TestCase {

  override val name: String = "Login Functionality Test Case"

  override def setupStep: SetupStep = new NoopSetupStep(profile)

  override def testStep: TestStep = new LoginStep(profile)

  override def cleanupStep: CleanupStep = new NoopCleanupSteps(profile)

}
