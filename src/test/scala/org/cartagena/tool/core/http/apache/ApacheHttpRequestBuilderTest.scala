package org.cartagena.tool.core.http.apache

import org.apache.http.entity.StringEntity
import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.http.apache.ApacheHttpRequestMatchers._
import org.scalatest.{FlatSpec, Matchers}

class ApacheHttpRequestBuilderTest extends FlatSpec with Matchers {

  private val REQUEST_ID = 1
  private val URL_STRING = "http://www.google.com"
  private val HEADER = "header1" -> "value1"
  private val PARAM = "param1" -> "value1"
  private val BODY_CONTENT = "This is some body"

  "buildHttpGet" should "create HTTP GET request" in {
    // when
    val request = ApacheHttpRequestBuilder[CannotHaveEntity]()
      .withId(REQUEST_ID)
      .withURL(URL_STRING)
      .withHeaders(Map(HEADER))
      .withParams(Map(PARAM))
      .buildHttpGet()

    // then
    request should haveId(REQUEST_ID)
    request should haveURL(URL_STRING)
    request should containHeaders(Map(HEADER))
    request should containParams(Map(PARAM))
    request should notHaveBody
  }

  "buildHttpPost" should "create HTTP POST request" in {
    // when
    val request = ApacheHttpRequestBuilder[MustHaveEntity]()
      .withId(REQUEST_ID)
      .withURL(URL_STRING)
      .withHeaders(Map(HEADER))
      .withParams(Map(PARAM))
      .withEntity(new StringEntity(BODY_CONTENT))
      .buildHttpPost()

    // then
    request should haveId(REQUEST_ID)
    request should haveURL(URL_STRING)
    request should containHeaders(Map(HEADER))
    request should containParams(Map(PARAM))
    request should haveBody(BODY_CONTENT)
  }

  "buildHttpPut" should "create HTTP PUT request" in {
    // when
    val request = ApacheHttpRequestBuilder[MustHaveEntity]()
      .withId(REQUEST_ID)
      .withURL(URL_STRING)
      .withHeaders(Map(HEADER))
      .withParams(Map(PARAM))
      .withEntity(new StringEntity(BODY_CONTENT))
      .buildHttpPut()

    // then
    request should haveId(REQUEST_ID)
    request should haveURL(URL_STRING)
    request should containHeaders(Map(HEADER))
    request should containParams(Map(PARAM))
    request should haveBody(BODY_CONTENT)
  }

  "buildHttpDelete" should "create HTTP DELETE request" in {
    // when
    val request = ApacheHttpRequestBuilder[MayHaveEntity]()
      .withId(REQUEST_ID)
      .withURL(URL_STRING)
      .withHeaders(Map(HEADER))
      .withParams(Map(PARAM))
      .withEntity(None)
      .buildHttpDelete()

    // then
    request should haveId(REQUEST_ID)
    request should haveURL(URL_STRING)
    request should containHeaders(Map(HEADER))
    request should containParams(Map(PARAM))
    request should notHaveBody
  }

  it should "create HTTP DELETE request with body" in {
    // when
    val request = ApacheHttpRequestBuilder[MayHaveEntity]()
      .withId(REQUEST_ID)
      .withURL(URL_STRING)
      .withHeaders(Map(HEADER))
      .withParams(Map(PARAM))
      .withEntity(Some(new StringEntity(BODY_CONTENT)))
      .buildHttpDelete()

    // then
    request should haveId(REQUEST_ID)
    request should haveURL(URL_STRING)
    request should containHeaders(Map(HEADER))
    request should containParams(Map(PARAM))
    request should haveBody(BODY_CONTENT)
  }

}
