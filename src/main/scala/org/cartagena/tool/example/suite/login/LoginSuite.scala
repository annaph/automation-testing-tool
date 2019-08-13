package org.cartagena.tool.example.suite.login

import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.model._
import org.cartagena.tool.example.suite.login.testcase.CreateSession

case object LoginSuite extends Suite {

  override def name: String = "Login suite"

  override def setupSteps: SerialSetupStep =
    StartRestClient

  override def testCases: List[TestCase] =
    CreateSession :: Nil

  override def cleanupSteps: SerialCleanupStep =
    ShutdownRestClient

}
