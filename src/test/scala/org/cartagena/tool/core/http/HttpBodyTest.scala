package org.cartagena.tool.core.http

import org.cartagena.tool.core.http.HttpBody.{EMPTY_BODY_CONTENT, JSON_STRING_INVALID_ERR_MSG}
import org.scalatest.{FlatSpec, Matchers}

class HttpBodyTest extends FlatSpec with Matchers {

  private val SOME_TEXT = "This is some text"

  private val SOME_JSON_STRING = """{"field1": 1,"field2": "str","field3": null}"""

  "isValid" should "return true for Text Http body type" in {
    // given
    val text = Text(SOME_TEXT)

    // then
    text.isValid should be(true)
  }

  it should "return true for Empty Http body type" in {
    // given
    val empty = Empty

    // then
    empty.isValid should be(true)
  }

  it should "return true for valid JsonString Http body type" in {
    // given
    val jsonString = JsonString(SOME_JSON_STRING)

    // then
    jsonString.isValid should be(true)
  }

  it should "cause exception when creating invalid JsonString http body type" in {
    val caught = intercept[IllegalArgumentException] {
      // when
      JsonString("invalid_json_string")
    }

    // then
    caught.getMessage should be(s"requirement failed: $JSON_STRING_INVALID_ERR_MSG")
  }

  "toPrettyString" should "prettify Text Http body type" in {
    // given
    val text = Text(SOME_TEXT)

    // when
    val actual = text.toPrettyString

    // then
    actual should be(SOME_TEXT)
  }

  it should "prettify JsonString Http body type" in {
    // given
    val jsonString = JsonString(SOME_JSON_STRING)
    val expected = "{\n  \"field1\" : 1,\n  \"field2\" : \"str\",\n  \"field3\" : null\n}"

    // when
    val actual = jsonString.toPrettyString

    // then
    actual should be(expected)
  }

  it should "prettify Empty Http body type" in {
    // given
    val empty = Empty

    // when
    val actual = empty.toPrettyString

    // then
    actual should be(EMPTY_BODY_CONTENT)
  }

}
