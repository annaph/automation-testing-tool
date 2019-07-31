package org.cartagena.tool.core.http.json4s

import org.cartagena.tool.core.http.json4s.Json4sTestUtil._
import org.cartagena.tool.core.registry.Json4sRegistryTest
import org.scalatest.{FlatSpec, Matchers}

class Json4sClientTest extends FlatSpec with Matchers with Json4sRegistryTest {

  override private[core] val json4sClient = new Json4sClientWithMyDefaultFormats

  "formats" should "return configured Json4s Formats" in {
    // when
    val actual = json4sClient.formats

    // then
    actual shouldBe a[MyFormats1.type]
  }

  "setFormats" should "set new Json4s Formats" in {
    // given
    val newFormats = MyFormats2

    // when
    json4sClient setFormats newFormats

    // then
    json4sClient.formats shouldBe a[MyFormats2.type]
  }

  "addSerializers" should "add custom Json serializers to Json4s Formats" in {
    // given
    val serializers = MySerializer1 :: MySerializer2 :: Nil

    // when
    json4sClient addSerializers serializers

    // then
    json4sClient.formats.customSerializers should contain theSameElementsInOrderAs serializers
  }

}
