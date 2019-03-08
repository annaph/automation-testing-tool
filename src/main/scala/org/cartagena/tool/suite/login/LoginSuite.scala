package org.cartagena.tool.suite.login

import org.cartagena.tool.core.model.{Suite, TestCase}
import org.cartagena.tool.suite.login.LoginTestCases.CreateSession

case object LoginSuite extends Suite {

  override def name: String = "Login suite"

  override def testCases: List[TestCase] = List(CreateSession)

}
