package org.cartagena.tool.suite.login

import org.cartagena.tool.core.http.apache.ApacheRestHelper
import org.cartagena.tool.core.http.{HttpGet, HttpRequest, EmptyBody}
import org.cartagena.tool.core.model.{AbstractTestStep, TestStep}

object LoginSteps {

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
        `type` = HttpGet,
        headers = Map(
          "Accept" -> headerAccept,
          "Content-Type" -> headerContentType),
        params = Map(
          "username" -> username,
          "password" -> password)
      )

      println(request)

      val response = execute(request)

      ???
    }

    override def nextTestStep: TestStep = AssertJsonResponse

  }

  case object AssertJsonResponse extends AbstractTestStep(profile, context) {

    override val name: String = "Assert JSON response"

    override def run(): Unit =
      println("There is work to be done here...\n")

  }

}
