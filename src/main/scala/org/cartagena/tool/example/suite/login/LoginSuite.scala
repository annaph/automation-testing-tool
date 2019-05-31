package org.cartagena.tool.example.suite.login

import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.model._
import org.cartagena.tool.example.suite.login.LoginSteps.{ShutdownRestClient, StartRestClient}
import org.cartagena.tool.example.suite.login.LoginTestCases.CreateSession

case object LoginSuite extends Suite {

  override def name: String = "Login suite"

  override def setupSteps: SerialSetupStep =
    StartRestClient

  override def testCases: List[TestCase] =
    List(CreateSession)

  override def cleanupSteps: SerialCleanupStep =
    ShutdownRestClient

}
