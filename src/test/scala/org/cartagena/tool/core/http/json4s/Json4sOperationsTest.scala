package org.cartagena.tool.core.http.json4s

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.cartagena.tool.core.http.json4s.Json4sTestUtil.{FLAG_VALUE, INT_VALUE, MyDomainEntity, STR_VALUE, json, MyFormats1 => MyFormats}
import org.cartagena.tool.core.registry.Json4sRegistryTest
import org.json4s.Formats
import org.scalatest.{FlatSpec, Matchers}

class Json4sOperationsTest extends FlatSpec with Matchers with Json4sRegistryTest {

  override private[core] val json4sOperations = new Json4sOperationsImpl

  implicit val formats: Formats = MyFormats

  "parse" should "parse Json String to domain entity" in {
    // given
    val jsonString = json

    // when
    val actual: MyDomainEntity = json4sOperations parse[MyDomainEntity] jsonString

    // then
    actual should be(MyDomainEntity(STR_VALUE, INT_VALUE, FLAG_VALUE))
  }

  it should "parse Json Input Stream to domain entity" in {
    // given
    val jsonInputStream = IOUtils toInputStream(json, StandardCharsets.UTF_8.name())

    // when
    val actual: MyDomainEntity = json4sOperations parse[MyDomainEntity] jsonInputStream

    // then
    actual should be(MyDomainEntity(STR_VALUE, INT_VALUE, FLAG_VALUE))
  }

}
