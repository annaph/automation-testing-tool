package org.cartagena.tool.core.http.json4s

import org.cartagena.tool.core.agent.Json4sAgentTest
import org.scalatest.{FlatSpec, Matchers}

class Json4sHelperTest extends FlatSpec with Matchers with Json4sAgentTest {

  override private[core] val json4sHelper =
    new Json4sHelperImpl(json4sClient, json4sOperations)

  "useJsonFormats" should "set Json Formats to be available for usage" in {
    // given

    // when
    json4sHelper.useJsonFormats(???)

    // then

    ???
  }

  "useJsonSerializer" should "set custom Json serializer to be available for usage" in {
    // given

    // when

    // then

    ???
  }

  "useJsonSerializers" should "set custom Json serializers to be available for usage" in {
    // given

    // when

    // then

    ???
  }

  "parse" should "parse JsonString to domain entity" in {
    // given

    // when

    // then

    ???
  }

  it should "parse Json Input Stream to domain entity" in {
    // given

    // when

    // then

    ???
  }

}
