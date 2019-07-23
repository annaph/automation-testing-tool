package org.cartagena.tool.core.http.json4s

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.cartagena.tool.core.agent.Json4sAgentTest
import org.cartagena.tool.core.http.json4s.Json4sOperationsTest._
import org.json4s.{DefaultFormats, Formats}
import org.scalatest.{FlatSpec, Matchers}

class Json4sOperationsTest extends FlatSpec with Matchers with Json4sAgentTest {

  override private[core] val json4sOperations =
    new Json4sOperationsImpl

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

object Json4sOperationsTest {

  implicit val formats: Formats = DefaultFormats

  val STR_VALUE = "some_string"

  val INT_VALUE = 12

  val FLAG_VALUE = true

  val json: String =
    s"""
       |{
       | "str": "$STR_VALUE",
       | "int": $INT_VALUE,
       | "flag": $FLAG_VALUE
       |}
    """.stripMargin

  case class MyDomainEntity(str: String, int: Int, flag: Boolean)

}
