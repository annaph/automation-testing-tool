package org.cartagena.tool.core.http.apache

import java.net.URL

import org.apache.http.entity.StringEntity
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
      .setId(REQUEST_ID)
      .setURL(new URL(URL_STRING))
      .setHeaders(Map(HEADER))
      .setParams(Map(PARAM))
      .buildHttpGet()

    // then
    request should haveId(REQUEST_ID)
    request should haveURL(URL_STRING)
    request should containHeaders(Map(HEADER))
    request should containParams(Map(PARAM))
  }

  "buildHttpPost" should "create HTTP POST request" in {
    // when
    val request = ApacheHttpRequestBuilder[CanHaveEntity]()
        .setId(REQUEST_ID)
        .setURL(new URL(URL_STRING))
        .setHeaders(Map(HEADER))
        .setParams(Map(PARAM))
        .setEntity(new StringEntity(BODY_CONTENT))
        .buildHttpPost()

    // then
    request should haveId(REQUEST_ID)
    request should haveURL(URL_STRING)
    request should containHeaders(Map(HEADER))
    request should containParams(Map(PARAM))
    request should haveBody(BODY_CONTENT)
  }

  it should "create HTTP POST request without entity" in {
    // when
    val request = ApacheHttpRequestBuilder[CannotHaveEntity]()
      .setId(REQUEST_ID)
      .setURL(new URL(URL_STRING))
      .setHeaders(Map(HEADER))
      .setParams(Map(PARAM))
      .buildHttpPost()

    // then
    request should haveId(REQUEST_ID)
    request should haveURL(URL_STRING)
    request should containHeaders(Map(HEADER))
    request should containParams(Map(PARAM))
    request should haveNoBody
  }

}
