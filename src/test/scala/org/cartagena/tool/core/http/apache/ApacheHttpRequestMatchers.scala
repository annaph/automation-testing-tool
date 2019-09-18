package org.cartagena.tool.core.http.apache

import java.net.URL

import org.apache.http.client.utils.URIBuilder
import org.scalatest.matchers.{MatchResult, Matcher}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

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

  def notHaveBody =
    new RequestHasNoBodyMatcher()

  class RequestHasIdMatcher(expectedId: Long) extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val actualId = left.id
      MatchResult(
        actualId == expectedId,
        s"Request ID '$actualId' is not equal to '$expectedId'",
        s"Request ID '$actualId' is equal to '$expectedId'")
    }

  }

  class RequestHasURLMatcher(expectedURL: URL) extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val actualURL = left.getURI.toURL.toString.split("\\?")(0)
      MatchResult(
        actualURL.toString == expectedURL.toString,
        s"Request URL '$actualURL' is not equal to '$expectedURL'",
        s"Request URL '$actualURL' is equal to '$expectedURL'")
    }

  }

  class RequestContainsHeaders(expectedHeaders: Map[String, String]) extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val actualHeaders = left.getAllHeaders
        .map(header => header.getName -> header.getValue)
        .toMap

      MatchResult(
        actualHeaders == expectedHeaders,
        s"Request headers '$actualHeaders' do not contain '$expectedHeaders'",
        s"Request headers '$actualHeaders' contain '$expectedHeaders'")
    }

  }

  class RequestContainsParams(expectedParams: Map[String, String]) extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val actualParams = new URIBuilder(left.getURI).getQueryParams.asScala
        .map(param => param.getName -> param.getValue)
        .toMap

      MatchResult(
        actualParams == expectedParams,
        s"Request query parameters '$actualParams' do not contain '$expectedParams'",
        s"Request query parameters '$actualParams' contain '$expectedParams'")
    }

  }

  class RequestHasBodyMatcher(expectedBody: String) extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val content = left.getEntity.getContent
      val actualBody: String = content

      MatchResult(
        actualBody == expectedBody,
        s"Request body '$actualBody' is not equal to '$expectedBody'",
        s"Request body '$actualBody' is equal to '$expectedBody'")
    }

  }

  class RequestHasNoBodyMatcher() extends Matcher[ApacheHttpRequest] {

    override def apply(left: ApacheHttpRequest): MatchResult = {
      val content = Try {
        left.getEntity
      } match {
        case Success(entity) =>
          Option(entity)
        case Failure(_) =>
          None
      }

      MatchResult(
        content.isEmpty,
        s"Request body is not empty",
        s"Request body is empty")
    }

  }

}
