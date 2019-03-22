package org.cartagena.tool.suite.login

import org.cartagena.tool.core.CartagenaConverters._
import org.cartagena.tool.core.http._
import org.cartagena.tool.core.http.apache.ApacheRestHelper
import org.cartagena.tool.core.http.json4s.Json4sHelper
import org.cartagena.tool.core.model.{AbstractCleanupStep, AbstractSetupStep, AbstractTestStep, TestStep}

object LoginSteps {

  case object StartRestClient extends AbstractSetupStep(profile, context) with ApacheRestHelper {

    override val name: String = "Start REST client"

    override def run(): Unit = startRestClient()

  }

  case object ExecuteHttpPutRequest extends AbstractTestStep(profile, context)
    with ApacheRestHelper
    with Json4sHelper {

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

      val response: HttpResponse[JsonString] = execute(request)

      response.cookies.find(_.name == "JSESSIONID").foreach {
        context ~=> SESSION_COOKIE />[Cookie] _
      }

      response.body.map(parse[LoginDTO]).foreach {
        context ~=> LOGIN_DTO />[LoginDTO] _
      }

      println("Response: " + response)
    }

    override def nextTestStep: TestStep = StoreSessionCookie

  }

  case object StoreSessionCookie extends AbstractTestStep(profile, context) with ApacheRestHelper {

    override val name: String = "Store session cookie"

    override def run(): Unit =
      storeCookie(context </[Cookie] SESSION_COOKIE)

    override def nextTestStep: TestStep = AssertJsonResponse

  }

  case object AssertJsonResponse extends AbstractTestStep(profile, context) with Json4sHelper {

    override val name: String = "Assert JSON response"
    private val expectedResult = "login.json"

    override def run(): Unit = {
      val expected = parse[LoginDTO](getClass.getResourceAsStream(expectedResult))
      val actual = context </[LoginDTO] LOGIN_DTO

      assert(
        actual.copy(timestamp = 1) == expected,
        "Login DTO not correct!")
    }

  }

  case object ShutdownRestClient extends AbstractCleanupStep(profile, context) with ApacheRestHelper {

    override val name: String = "Shutdown REST client"

    override def run(): Unit = shutdownRestClient()

  }

}
