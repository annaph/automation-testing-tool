package org.cartagena.tool.suite.login

import org.cartagena.tool.core.model.{TestCase, TestStep}
import org.cartagena.tool.suite.login.LoginSteps.ExecuteHttpPutRequest

object LoginTestCases {

  case object CreateSession extends TestCase {

    override val name: String = "Create session"

    override def firstStep: TestStep = ExecuteHttpPutRequest

  }

}
