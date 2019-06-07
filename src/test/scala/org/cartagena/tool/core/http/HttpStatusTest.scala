package org.cartagena.tool.core.http

import org.cartagena.tool.core.http.HttpStatus._
import org.scalatest.{FlatSpec, Matchers}

class HttpStatusTest extends FlatSpec with Matchers {

  "apply" should "create OK http status for code 200" in {
    // given
    val code = HTTP_STATUS_OK_CODE

    // when
    val actual = HttpStatus(code)

    // then
    actual should be(OK)
  }

  it should "create Created http status for code 201" in {
    // given
    val code = HTTP_STATUS_CREATED_CODE

    // when
    val actual = HttpStatus(code)

    // then
    actual should be(Created)
  }

  it should "create ServerError http status for code 500" in {
    // given
    val code = HTTP_STATUS_SERVER_ERROR_CODE

    // when
    val actual = HttpStatus(code)

    // then
    actual should be(ServerError)
  }

  it should "create Unsupported http status for code -1" in {
    // given
    val code = -1

    // when
    val actual = HttpStatus(code)

    // then
    actual should be(UnsupportedStatus)
  }

  "toPrettyString" should "prettify OK http status" in {
    // given
    val status = OK

    // when
    val actual = status.toPrettyString

    // then
    actual should be(HTTP_STATUS_OK)
  }

  it should "prettify Created http status" in {
    // given
    val status = Created

    // when
    val actual = status.toPrettyString

    // then
    actual should be(HTTP_STATUS_CREATED)
  }

  it should "prettify ServerError http status" in {
    // given
    val status = ServerError

    // when
    val actual = status.toPrettyString

    // then
    actual should be(HTTP_STATUS_SERVER_ERROR)
  }

  it should "prettify Unsupported http status" in {
    // given
    val status = UnsupportedStatus

    // when
    val actual = status.toPrettyString

    // then
    actual should be(HTTP_UNSUPPORTED_STATUS)
  }

}
