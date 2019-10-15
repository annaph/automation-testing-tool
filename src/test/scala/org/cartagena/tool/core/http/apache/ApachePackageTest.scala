package org.cartagena.tool.core.http.apache

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import org.apache.http.entity.StringEntity
import org.cartagena.tool.core.http._
import org.cartagena.tool.core.http.apache.ApacheHttpTestUtil._
import org.cartagena.tool.core.http.apache.ApachePackageTest._
import org.scalatest.{FlatSpec, Matchers}

class ApachePackageTest extends FlatSpec with Matchers {

  "intToHttpStatus" should "convert Int to HttpStatus" in {
    // given
    val code = STATUS_CODE_200

    // when
    val actual = intToHttpStatus(code)

    // then
    actual should be(OK)
  }

  "inputStreamToString" should "convert InputStream to String" in {
    // given
    val in = new ByteArrayInputStream(SOME_TEXT.getBytes(StandardCharsets.UTF_8.name()))

    // when
    val actual = inputStreamToString(in)

    // then
    actual should be(SOME_TEXT)
  }

  "nameValuePairsToMap" should "convert list of NameValuePair objects into Map" in {
    // given
    val nameValuePairs = List(
      NameValuePair(NAME_1, VALUE_1),
      NameValuePair(NAME_2, VALUE_2),
      NameValuePair(NAME_3, VALUE_3))

    val expected = Map(
      NAME_1 -> VALUE_1,
      NAME_2 -> VALUE_2,
      NAME_3 -> VALUE_3)

    // when
    val actual = nameValuePairsToMap(nameValuePairs)

    // then
    actual should be(expected)
  }

  "httpBodyToHttpEntity" should "convert Text Http body to Apache Http Entity" in {
    // given
    val body = Text(SOME_TEXT)

    //when
    val actual = httpBodyToHttpEntity(body)

    // then
    actual shouldBe a[StringEntity]
    inputStreamToString(actual.getContent) should be(SOME_TEXT)
  }

  it should "convert JsonString Http body to Apache Http Entity" in {
    // given
    val body = JsonString(SOME_JSON_STRING)

    // when
    val actual = httpBodyToHttpEntity(body)

    // then
    actual shouldBe a[StringEntity]
    inputStreamToString(actual.getContent) should be(SOME_JSON_STRING)
  }

  it should "convert Empty Http body to Apache Http Entity" in {
    // given
    val body = Empty

    // when
    val actual = httpBodyToHttpEntity(body)

    // then
    actual shouldBe a[StringEntity]
    inputStreamToString(actual.getContent).isEmpty should be(true)
  }

  "httpBodyToHttpEntityOption" should "convert Empty Http body to None HttpEntity" in {
    // given
    val body = Empty

    // when
    val actual = httpBodyToHttpEntityOption(body)

    // then
    actual should be(None)
  }

  it should "convert non-Empty Http body to Some HttpEntity" in {
    // given
    val body = Text(SOME_TEXT)

    // when
    val actual = httpBodyToHttpEntityOption(body)

    // then
    actual.isEmpty should be(false)
    actual.get shouldBe a[StringEntity]
    inputStreamToString(actual.get.getContent) should be(SOME_TEXT)
  }

  "toCookies" should "convert list of NameValuePair objects into List of Cookie objects" in {
    // given
    val nameValuePairs = List(
      NameValuePair(NAME_1, VALUE_1),
      NameValuePair(NAME_2, VALUE_2),
      NameValuePair(NAME_3, VALUE_3))

    val expected = List(
      Cookie(NAME_1, VALUE_1, COOKIE_DOMAIN, COOKIE_PATH),
      Cookie(NAME_2, VALUE_2, COOKIE_DOMAIN, COOKIE_PATH),
      Cookie(NAME_3, VALUE_3, COOKIE_DOMAIN, COOKIE_PATH))

    // when
    val actual = toCookies(nameValuePairs, COOKIE_DOMAIN, COOKIE_PATH)

    // then
    actual should contain theSameElementsInOrderAs expected
  }

}

object ApachePackageTest {

  val NAME_1 = "name1"
  val NAME_2 = "name2"
  val NAME_3 = "name3"

  val VALUE_1 = "value1"
  val VALUE_2 = "value2"
  val VALUE_3 = "value3"

}
