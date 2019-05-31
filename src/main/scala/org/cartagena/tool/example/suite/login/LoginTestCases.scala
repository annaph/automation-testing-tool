package org.cartagena.tool.example.suite.login

import org.cartagena.tool.core.model.{SerialTestStep, TestCase}
import org.cartagena.tool.example.suite.login.LoginSteps.{AssertJsonResponse, ExecuteHttpPutRequest, StoreSessionCookie}

object LoginTestCases {

  case object CreateSession extends TestCase {

    override val name: String = "Create session"

    override def testSteps: SerialTestStep = ExecuteHttpPutRequest +
      StoreSessionCookie +
      AssertJsonResponse

  }

}
