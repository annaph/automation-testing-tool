package org.cartagena.tool.core.http

import org.cartagena.tool.core.CartagenaUtils._
import org.scalatest.{FlatSpec, Matchers}

class HttpMessageTest extends FlatSpec with Matchers {

  private val _jsonString = JsonString("""{"field1": 1,"field2": "str","field3": null}""")

  "toPrettyString" should "prettify http request message" in {
    // given
    val request = HttpRequest(
      url = "http://www.google.com/",
      method = Post,
      headers = List(
        "header1" -> "header1_value",
        "header2" -> "header2_value",
        "header3" -> "header3_value"),
      params = List(
        "param1" -> "param1_value",
        "param2" -> "param2_value",
        "param3" -> "param3_value"),
      body = _jsonString)

    val expected = "=> HTTP request:\n" +
      "\tURL:\t\thttp://www.google.com/\n" +
      "\tMethod:\t\tPOST\n" +
      "\tHeaders:\n" +
      "\t\t\theader1: header1_value\n" +
      "\t\t\theader2: header2_value\n" +
      "\t\t\theader3: header3_value\n" +
      "\tParameters:\n" +
      "\t\t\tparam1 = param1_value\n" +
      "\t\t\tparam2 = param2_value\n" +
      "\t\t\tparam3 = param3_value\n" +
      "\tBody:\n" +
      "\t\t\t{\n" +
      "\t\t\t  \"field1\" : 1,\n" +
      "\t\t\t  \"field2\" : \"str\",\n" +
      "\t\t\t  \"field3\" : null\n" +
      "\t\t\t}"

    // when
    val actual = request.toPrettyString

    // then
    actual should be(expected)
  }

  it should "prettify http response message" in {
    // given
    val response = HttpResponse(
      status = OK,
      reason = "OK",
      cookies = List(
        Cookie("name1", "value1", "host1", "path1"),
        Cookie("name2", "value2", "host2", "path2"),
        Cookie("name3", "value3", "host3", "path3")),
      body = Some(_jsonString))

    val expected = "=> HTTP response:\n" +
      "\tStatus:\t\tOK\n" +
      "\tReason:\t\tOK\n" +
      "\tCookies:\n" +
      "\t\t\tname1: value1, value: value1, host: host1, path: path1\n" +
      "\t\t\tname2: value2, value: value2, host: host2, path: path2\n" +
      "\t\t\tname3: value3, value: value3, host: host3, path: path3\n" +
      "\tBody:\n" +
      "\t\t\t{\n" +
      "\t\t\t  \"field1\" : 1,\n" +
      "\t\t\t  \"field2\" : \"str\",\n" +
      "\t\t\t  \"field3\" : null\n" +
      "\t\t\t}"

    // when
    val actual = response.toPrettyString

    // then
    actual should be(expected)
  }

}
