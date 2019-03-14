package org.cartagena.tool.suite.login

import org.cartagena.tool.core.model.{CleanupStep, SetupStep, Suite, TestCase}
import org.cartagena.tool.suite.login.LoginSteps.{ShutdownRestClient, StartRestClient}
import org.cartagena.tool.suite.login.LoginTestCases.CreateSession

case object LoginSuite extends Suite {

  override def name: String = "Login suite"

  override def setupStep: SetupStep = StartRestClient

  override def testCases: List[TestCase] = List(CreateSession)

  override def cleanupStep: CleanupStep = ShutdownRestClient

}
