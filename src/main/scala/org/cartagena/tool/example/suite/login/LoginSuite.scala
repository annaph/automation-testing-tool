package org.cartagena.tool.example.suite.login

import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.http.apache.ApacheRestHelper
import org.cartagena.tool.core.model.{ShapelessCleanupStep => CleanupStep, ShapelessSetupStep => SetupStep, _}
import org.cartagena.tool.example.suite.login.testcase.CreateSession

case object LoginSuite extends Suite {

  override def name: String = "Login suite"

  override def setupSteps: SerialSetupStep =
    StartRestClient

  override def testCases: List[TestCase] =
    List(CreateSession)

  override def cleanupSteps: SerialCleanupStep =
    ShutdownRestClient

}

case object StartRestClient extends SetupStep
  with LoginProfileAndContext
  with ApacheRestHelper {

  override val name: String = "Start REST client"

  override def run(): Unit =
    startRestClient()

}

case object ShutdownRestClient extends CleanupStep
  with ApacheRestHelper {

  override val name: String = "Shutdown REST client"

  override def run(): Unit =
    shutdownRestClient()

}
