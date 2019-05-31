package org.cartagena.tool.example.suite.login

import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.agent.JsonAgent
import org.cartagena.tool.core.http._
import org.cartagena.tool.core.http.apache.ApacheRestHelper
import org.cartagena.tool.core.model.{ShapelessCleanupStep => CleanupStep, ShapelessSetupStep => SetupStep, ShapelessTestStep => TestStep}
import org.cartagena.tool.example.suite.login.model.LoginDTO

object LoginSteps {

  case object StartRestClient extends SetupStep
    with LoginProfileAndContext
    with ApacheRestHelper {

    override val name: String = "Start REST client"

    override def run(): Unit =
      startRestClient()

  }

  case object ExecuteHttpPutRequest extends TestStep
    with LoginProfileAndContext
    with ApacheRestHelper {

    override val name: String = "Execute HTTP PUT request"

    private val host = profile getProperty("host", "")
    private val port = profile getProperty("port", "")
    private val username = profile getProperty("username", "")
    private val password = profile getProperty("password", "")

    private val headerAccept = context </[String] HEADER_ACCEPT
    private val headerContentType = context </[String] HEADER_CONTENT_TYPE

    override def run(): Unit = {
      val request = HttpRequest(
        url = s"http://$host:$port/j_spring_security_check",
        method = Post,
        headers = List(
          "Accept" -> headerAccept,
          "Content-Type" -> headerContentType),
        params = List(
          "username" -> username,
          "password" -> password),
        body = EmptyBody)

      print(request)

      val response = execute[EmptyBody.type, JsonString](request)
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

  case object StoreSessionCookie extends TestStep
    with LoginProfileAndContext
    with ApacheRestHelper {

    override val name: String = "Store session cookie"

    override def run(): Unit = {
      val response: HttpResponse[JsonString] = context </[HttpResponse[JsonString]] LOGIN_RESPONSE

      response.cookies.find(_.name == "JSESSIONID") match {
        case Some(cookie) =>
          storeCookie(cookie)
        case None =>
          throw new Exception("No session cookie to store!")
      }
    }

  }

  case object AssertJsonResponse extends TestStep
    with LoginProfileAndContext
    with JsonAgent {

    override val name: String = "Assert JSON response"
    private val expectedResult = "login.json"

    override def run(): Unit = {
      val response: HttpResponse[JsonString] = context </[HttpResponse[JsonString]] LOGIN_RESPONSE

      response.body match {
        case Some(b) =>
          val expected = jsonHelper parse[LoginDTO] getClass.getResourceAsStream(expectedResult)
          val actual: LoginDTO = jsonHelper parse[LoginDTO] b

          assert(
            actual.copy(timestamp = 1) == expected,
            "Login DTO not correct!")
        case None =>
          throw new Exception("No response body to assert!")
      }
    }

  }

  case object ShutdownRestClient extends CleanupStep
    with ApacheRestHelper {

    override val name: String = "Shutdown REST client"

    override def run(): Unit =
      shutdownRestClient()

  }

}
