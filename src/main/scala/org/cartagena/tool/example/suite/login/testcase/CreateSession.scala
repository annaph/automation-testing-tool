package org.cartagena.tool.example.suite.login.testcase

import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.agent.{JsonAgent, RestAgent}
import org.cartagena.tool.core.http.{HttpRequest, _}
import org.cartagena.tool.core.model.{SerialTestStep, TestCase, ShapelessTestStep => TestStep}
import org.cartagena.tool.example.suite.login.model.LoginDTO
import org.cartagena.tool.example.suite.login.{LoginProfileAndContext, _}

case object CreateSession extends TestCase {

  override val name: String = "Create session"

  override def testSteps: SerialTestStep =
    ExecuteHttpPutRequest +
      StoreSessionCookie +
      AssertJsonResponse

}

case object ExecuteHttpPutRequest
  extends TestStep
    with LoginProfileAndContext
    with RestAgent {

  override val name: String = "Execute HTTP PUT request"

  private val host = profile getProperty "host" getOrElse ""
  private val port = profile getProperty "port" getOrElse ""
  private val username = profile getProperty "username" getOrElse ""
  private val password = profile getProperty "password" getOrElse ""

  private val headerAccept = context </[String] HEADER_ACCEPT
  private val headerContentType = context </[String] HEADER_CONTENT_TYPE

  override def run(): Unit = {
    val request = HttpRequest(
      url = s"http://$host:$port/j_spring_security_check",
      method = Post,
      headers = ("Accept" -> headerAccept) + ("Content-Type" -> headerContentType),
      params = ("username" -> username) + ("password" -> password),
      body = Empty)

    print(request)

    val response = restHelper execute[Empty.type, JsonString] request
    print(response)

    assert(
      response.status == OK,
      "Http status code must be 200!")

    assert(
      response.reason == "OK",
      "Http reason must be OK!")

    context ~=> LOGIN_RESPONSE />[HttpResponse[JsonString]] response
  }

}

case object StoreSessionCookie
  extends TestStep
    with LoginProfileAndContext
    with RestAgent {

  override val name: String = "Store session cookie"

  override def run(): Unit = {
    val response: HttpResponse[JsonString] = context </[HttpResponse[JsonString]] LOGIN_RESPONSE

    response.cookies.find(_.name == "JSESSIONID") match {
      case Some(cookie) =>
        restHelper storeCookie cookie
      case None =>
        throw new Exception("No session cookie to store!")
    }
  }

}

case object AssertJsonResponse
  extends TestStep
    with LoginProfileAndContext
    with JsonAgent {

  override val name: String = "Assert JSON response"
  private val expectedResult = "login.json"

  override def run(): Unit = {
    val response: HttpResponse[JsonString] = context </[HttpResponse[JsonString]] LOGIN_RESPONSE

    val expected = jsonHelper parse[LoginDTO] getClass.getResourceAsStream(expectedResult)
    val actual: LoginDTO = jsonHelper parse[LoginDTO] response.body

    assert(
      actual.copy(timestamp = 1) == expected,
      "Login DTO not correct!")
  }

}
