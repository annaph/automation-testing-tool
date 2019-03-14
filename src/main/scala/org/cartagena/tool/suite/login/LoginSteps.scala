package org.cartagena.tool.suite.login

import java.net.URL

import org.cartagena.tool.core.http.apache.ApacheRestHelper
import org.cartagena.tool.core.http._
import org.cartagena.tool.core.model.{AbstractCleanupStep, AbstractSetupStep, AbstractTestStep, TestStep}
import org.cartagena.tool.core.CartagenaConverters._

object LoginSteps {

  case object StartRestClient extends AbstractSetupStep(profile, context) with ApacheRestHelper {

    override val name: String = "Start REST client"

    override def run(): Unit = startRestClient()

  }

  case object ExecuteHttpPutRequest extends AbstractTestStep(profile, context) with ApacheRestHelper {

    override val name: String = "Execute HTTP PUT request"

    private val host = profile.getProperty("host").getOrElse("")
    private val port = profile.getProperty("port").getOrElse("")
    private val username = profile.getProperty("username").getOrElse("")
    private val password = profile.getProperty("password").getOrElse("")
    private val headerAccept = profile.getProperty("header.accept").getOrElse("")
    private val headerContentType = profile.getProperty("header.accept").getOrElse("")

    override def run(): Unit = {
      println("There is work to be done here...\n")

      val request = HttpRequest[EmptyBody.type](
        url = s"http://$host:$port/j_spring_security_check",
        method = HttpPost,
        headers = List[HeaderElement](
          HeaderElement("Accept", headerAccept),
          HeaderElement("Content-Type", headerContentType)),
        params = List(
          QueryParam("username", username),
          QueryParam("password", password))
      )

      println(request)

      val response: HttpResponse[JsonString] = execute(request)
      println("Response: " + response)
    }

    override def nextTestStep: TestStep = AssertJsonResponse

  }

  case object AssertJsonResponse extends AbstractTestStep(profile, context) {

    override val name: String = "Assert JSON response"

    override def run(): Unit =
      println("There is work to be done here...\n")

  }

  case object ShutdownRestClient extends AbstractCleanupStep(profile, context) with ApacheRestHelper {

    override val name: String = "Shutdown REST client"

    override def run(): Unit = shutdownRestClient()

  }

}
