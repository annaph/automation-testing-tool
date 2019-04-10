package org.cartagena.tool.suite.login

import org.cartagena.tool.core.CartagenaConverters._
import org.cartagena.tool.core.agent.JsonAgent
import org.cartagena.tool.core.http._
import org.cartagena.tool.core.http.apache.ApacheRestHelper
import org.cartagena.tool.core.model.{AbstractCleanupStep, AbstractSetupStep, AbstractTestStep, TestStep}

object LoginSteps {

  case object StartRestClient extends AbstractSetupStep(profile, context) with ApacheRestHelper {

    override val name: String = "Start REST client"

    override def run(): Unit = startRestClient()

  }

  case object ExecuteHttpPutRequest extends AbstractTestStep(profile, context) with ApacheRestHelper {

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
        method = HttpPost,
        headers = List(
          "Accept" -> headerAccept,
          "Content-Type" -> headerContentType),
        params = List(
          "username" -> username,
          "password" -> password),
        body = EmptyBody)

      println(request)

      val response = execute[EmptyBody.type, JsonString](request)
      println(response)

      assert(
        response.status == HttpStatusOK,
        "Http status code must be 200!")

      assert(
        response.reason == "OK",
        "Http reason must be OK!")

      context ~=> LOGIN_RESPONSE />[HttpResponse[JsonString]] response
    }

    override def nextTestStep: TestStep = StoreSessionCookie

  }

  case object StoreSessionCookie extends AbstractTestStep(profile, context) with ApacheRestHelper {

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

    override def nextTestStep: TestStep = AssertJsonResponse

  }

  case object AssertJsonResponse extends AbstractTestStep(profile, context) with JsonAgent {

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

  case object ShutdownRestClient extends AbstractCleanupStep(profile, context) with ApacheRestHelper {

    override val name: String = "Shutdown REST client"

    override def run(): Unit = shutdownRestClient()

  }

}
