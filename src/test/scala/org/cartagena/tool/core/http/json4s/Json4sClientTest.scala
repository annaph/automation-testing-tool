package org.cartagena.tool.core.http.json4s

import org.cartagena.tool.core.agent.Json4sAgentTest
import org.cartagena.tool.core.http.json4s.Json4sTestUtil._
import org.scalatest.{FlatSpec, Matchers}

class Json4sClientTest extends FlatSpec with Matchers with Json4sAgentTest {

  override private[core] val json4sClient =
    new Json4sClientWithMyDefaultFormats

  "formats" should "return configured Json4s Formats" in {
    // when
    val actual = json4sClient.formats

    // then
    actual shouldBe a[MyDefaultFormats1.type]
  }

  "setFormats" should "set new Json4s Formats" in {
    // given
    val newFormats = MyDefaultFormats2

    // when
    json4sClient setFormats newFormats

    // then
    json4sClient.formats shouldBe a[MyDefaultFormats2.type]
  }

  "addSerializers" should "add custom Json serializers to Json4s Formats" in {
    // given
    val serializers = MyCustomSerializer1 :: MyCustomSerializer2 :: Nil

    // when
    json4sClient addSerializers serializers

    val customSerializers = json4sClient.formats.customSerializers

    // then
    json4sClient.formats.customSerializers should contain theSameElementsInOrderAs serializers
  }

}
