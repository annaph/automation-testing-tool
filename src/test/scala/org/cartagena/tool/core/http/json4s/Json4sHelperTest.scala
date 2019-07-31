package org.cartagena.tool.core.http.json4s

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.cartagena.tool.core.http.JsonString
import org.cartagena.tool.core.http.json4s.Json4sTestUtil.{FLAG_VALUE, INT_VALUE, MyDomainEntity, MyFormats2, MySerializer1, MySerializer2, STR_VALUE, json, MyFormats1 => MyFormats}
import org.cartagena.tool.core.registry.Json4sRegistryTest
import org.json4s.Formats
import org.mockito.Mockito.{doNothing, verify, when}
import org.scalatest.{FlatSpec, Matchers}

class Json4sHelperTest extends FlatSpec with Matchers with Json4sRegistryTest {

  override private[core] val json4sHelper = new Json4sHelperImpl(json4sClient, json4sOperations)

  implicit val mf: Manifest[MyDomainEntity] = Manifest.classType(classOf[MyDomainEntity])

  implicit val formats: Formats = MyFormats

  "useJsonFormats" should "set Json Formats to be available for usage" in {
    // given
    val formats = MyFormats2

    doNothing().when(json4sClient).setFormats(formats)

    // when
    json4sHelper useJsonFormats formats

    // then
    verify(json4sClient).setFormats(formats)
  }

  "useJsonSerializer" should "set custom Json serializer to be available for usage" in {
    // given
    val serializer = MySerializer1

    doNothing().when(json4sClient).addSerializers(MySerializer1 :: Nil)

    // when
    json4sHelper useJsonSerializer serializer

    // then
    verify(json4sClient).addSerializers(MySerializer1 :: Nil)
  }

  "useJsonSerializers" should "set custom Json serializers to be available for usage" in {
    // given
    val serializers = MySerializer1 :: MySerializer2 :: Nil

    doNothing().when(json4sClient).addSerializers(serializers)

    // when
    json4sHelper useJsonSerializers serializers

    // then
    verify(json4sClient).addSerializers(serializers)
  }

  "parse" should "parse JsonString to domain entity" in {
    // given
    val jsonString = JsonString(json)
    val domainEntity = MyDomainEntity(STR_VALUE, INT_VALUE, FLAG_VALUE)

    when(json4sClient.formats)
      .thenReturn(formats)

    when(json4sOperations.parse(json)(mf, formats))
      .thenReturn(domainEntity)

    // when
    val actual: MyDomainEntity = json4sHelper parse[MyDomainEntity] jsonString

    // then
    actual should be(domainEntity)

    verify(json4sOperations).parse(json)(mf, formats)
  }

  it should "parse Json Input Stream to domain entity" in {
    // given
    val jsonInputStream = IOUtils toInputStream(json, StandardCharsets.UTF_8.name())
    val domainEntity = MyDomainEntity(STR_VALUE, INT_VALUE, FLAG_VALUE)

    when(json4sClient.formats)
      .thenReturn(formats)

    when(json4sOperations.parse(jsonInputStream)(mf, formats))
      .thenReturn(domainEntity)

    // when
    val actual: MyDomainEntity = json4sHelper parse[MyDomainEntity] jsonInputStream

    // then
    actual should be(domainEntity)

    verify(json4sOperations).parse(jsonInputStream)(mf, formats)
  }

}
