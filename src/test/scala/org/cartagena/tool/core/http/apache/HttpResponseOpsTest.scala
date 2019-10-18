package org.cartagena.tool.core.http.apache

import org.apache.http.entity.StringEntity
import org.apache.http.message.{BasicHeaderElement, BasicHttpResponse}
import org.apache.http.{HttpResponse, ProtocolVersion, HeaderElement => ApacheHeaderElement}
import org.cartagena.tool.core.http.apache.ApacheHttpResponse.HttpResponseOps
import org.cartagena.tool.core.http.apache.ApacheHttpTestUtil._
import org.cartagena.tool.core.http.{Empty, JsonString, NameValuePair, Text}
import org.mockito.Mockito.{spy, when}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class HttpResponseOpsTest extends FlatSpec with Matchers with MockitoSugar with BeforeAndAfter {

  private var httpResponse: HttpResponse = _

  before {
    httpResponse = new BasicHttpResponse(
      new ProtocolVersion(PROTOCOL, PROTOCOL_VERSION, PROTOCOL_VERSION), STATUS_CODE_200, REASON_PHRASE_OK)
  }

  "statusCode" should "return status code 200" in {
    // when
    val actual = httpResponse.statusCode

    // then
    actual should be(STATUS_CODE_200.toInt)
  }

  "reasonPhrase" should "return reason phrase OK" in {
    // when
    val actual = httpResponse.reasonPhrase

    // then
    actual should be(REASON_PHRASE_OK)
  }

  "cookieHeaderElements" should "return list of cookie header elements" in {
    // given
    val cookieHeader = spy(CookieHeader)
    val cookieHeaderElements = Array[ApacheHeaderElement](new BasicHeaderElement(COOKIE_NAME, COOKIE_VALUE))

    when(cookieHeader.getElements).thenReturn(cookieHeaderElements)

    val expected = NameValuePair(COOKIE_NAME, COOKIE_VALUE) :: Nil

    // when
    httpResponse addHeader cookieHeader
    val actual = httpResponse.cookieHeaderElements

    // then
    actual should contain theSameElementsAs expected
  }

  "httpBody" should "return Text Http body" in {
    // given
    val entity = new StringEntity(SOME_TEXT)

    // when
    httpResponse setEntity entity
    val actual = httpResponse.httpBody[Text]

    // then
    actual should be(Text(SOME_TEXT))
  }

  it should "return JsonString HttpBody" in {
    // given
    val entity = new StringEntity(SOME_JSON_STRING)

    // when
    httpResponse setEntity entity
    val actual = httpResponse.httpBody[JsonString]

    // then
    actual should be(JsonString(SOME_JSON_STRING))
  }

  it should "return Empty Http body" in {
    // when
    val actual = httpResponse.httpBody[Empty.type]

    // then
    actual should be(Empty)
  }

}
