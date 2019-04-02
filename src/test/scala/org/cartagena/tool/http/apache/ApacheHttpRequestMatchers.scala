package org.cartagena.tool.core.http.apache

import java.net.URL
import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.apache.http.client.utils.URIBuilder
import org.scalatest.matchers.{MatchResult, Matcher}

import scala.collection.JavaConverters._

object ApacheHttpRequestMatchers {

  def haveId(expectedId: Long) =
    new RequestHasIdMatcher(expectedId)

  def haveURL(expectedURL: String) =
    new RequestHasURLMatcher(new URL(expectedURL))

  def containHeaders(expectedHeaders: Map[String, String]) =
    new RequestContainsHeaders(expectedHeaders)

  def containParams(expectedParams: Map[String, String]) =
    new RequestContainsParams(expectedParams)

  def haveBody(expectedBody: String) =
    new RequestHasBodyMatcher(expectedBody)

  def haveNoBody =
    new RequestHasNoBodyMatcher

  class RequestHasIdMatcher(expectedId: Long) extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val actualId = left.id
      MatchResult(
        actualId == expectedId,
        s"Request ID '$actualId' did not equal to '$expectedId'",
        s"Request ID '$actualId' did equal to '$expectedId'")
    }

  }

  class RequestHasURLMatcher(expectedURL: URL) extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val actualURL = left.getURI.toURL.toString.split("\\?")(0)
      MatchResult(
        actualURL.toString == expectedURL.toString,
        s"Request URL '$actualURL' did not equal to '$expectedURL'",
        s"Request URL '$actualURL' did equal to '$expectedURL'")
    }

  }

  class RequestContainsHeaders(expectedHeaders: Map[String, String]) extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val actualHeaders = left.getAllHeaders
        .map(header => header.getName -> header.getValue)
        .toMap

      MatchResult(
        actualHeaders == expectedHeaders,
        s"Request headers '$actualHeaders' did not contain '$expectedHeaders'",
        s"Request headers '$actualHeaders' did contain '$expectedHeaders'")
    }
  }

  class RequestContainsParams(expectedParams: Map[String, String]) extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val actualParams = new URIBuilder(left.getURI).getQueryParams.asScala
        .map(param => param.getName -> param.getValue)
        .toMap

      MatchResult(
        actualParams == expectedParams,
        s"Request query parameters '$actualParams' did not contain '$expectedParams'",
        s"Request query parameters '$actualParams' did contain '$expectedParams'")
    }
  }

  class RequestHasBodyMatcher(expectedBody: String) extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val content = left.getEntity.getContent
      val actualBody = IOUtils toString(content, StandardCharsets.UTF_8.name())

      MatchResult(
        actualBody == expectedBody,
        s"Request body '$actualBody' did not equal to '$expectedBody'",
        s"Request body '$actualBody' did equal to '$expectedBody'")
    }

  }


  class RequestHasNoBodyMatcher extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val content = Option(left.getEntity)

      MatchResult(
        content.isEmpty,
        s"Request body is not empty",
        s"Request body is empty")
    }
  }

}
