package org.cartagena.tool.core.http.apache

import org.apache.http.entity.StringEntity
import org.cartagena.tool.core.http.apache.ApacheHttpBodyConverter.{EMPTY_STRING, EmptyApacheHttpBodyConverter, JsonStringApacheHttpBodyConverter, TextApacheHttpBodyConverter}
import org.cartagena.tool.core.http.apache.ApacheHttpTestUtil.{SOME_JSON_STRING, SOME_TEXT}
import org.cartagena.tool.core.http.{Empty, JsonString, Text}
import org.scalatest.{FlatSpec, Matchers}

class ApacheHttpBodyConverterTest extends FlatSpec with Matchers {

  "fromHttpEntity" should "convert from Apache Http Entity to Text Http body" in {
    // given
    val converter = TextApacheHttpBodyConverter
    val entity = new StringEntity(SOME_TEXT)

    // when
    val actual = converter fromHttpEntity entity

    // then
    actual should be(Text(SOME_TEXT))
  }

  it should "convert from Apache Http Entity to JsonString Http body" in {
    // given
    val converter = JsonStringApacheHttpBodyConverter
    val entity = new StringEntity(SOME_JSON_STRING)

    // when
    val actual = converter fromHttpEntity entity

    // then
    actual should be(JsonString(SOME_JSON_STRING))
  }

  it should "convert from Apache Http Entity to Empty Http body" in {
    // given
    val converter = EmptyApacheHttpBodyConverter
    val entity = new StringEntity(EMPTY_STRING)

    // when
    val actual = converter fromHttpEntity entity

    // then
    actual should be(Empty)
  }

  "toHttpEntity" should "convert Text Http body to Apache Http Entity" in {
    // given
    val converter = TextApacheHttpBodyConverter
    val body = Text(SOME_TEXT)

    // when
    val actual: String = (converter toHttpEntity body).getContent

    // then
    actual should be(SOME_TEXT)
  }

  it should "convert JsonString Http body to Apache Http Entity" in {
    // given
    val converter = JsonStringApacheHttpBodyConverter
    val body = JsonString(SOME_JSON_STRING)

    // when
    val actual: String = (converter toHttpEntity body).getContent

    // then
    actual should be(SOME_JSON_STRING)
  }

  it should "convert Empty Http body to Apache Http Entity" in {
    // given
    val converter = EmptyApacheHttpBodyConverter
    val body = Empty

    // when
    val actual: String = (converter toHttpEntity body).getContent

    // then
    actual.isEmpty should be(true)
  }

}
