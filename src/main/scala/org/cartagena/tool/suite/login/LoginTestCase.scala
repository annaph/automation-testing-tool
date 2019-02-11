package org.cartagena.tool.suite.login

import org.cartagena.tool.core.model.{Profile, TestCase, TestStep}
import org.cartagena.tool.suite.login.Steps._

case class LoginTestCase(profile: Profile) extends TestCase {

  override val name: String = "Login Functionality Test Case"

  override def firstStep: TestStep = new LoginStep(profile)

}
