package org.cartagena.tool.core.http.json4s

import org.json4s.JsonAST.JNull
import org.json4s.{CustomSerializer, DefaultFormats}
import scalaz.effect.STRef

object Json4sTestUtil {

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

  class Json4sClientWithMyDefaultFormats extends Json4sClientImpl {

    override private[json4s] val formatsRef =
      STRef[Nothing](MyFormats1)

  }

  case class MyDomainEntity(str: String, int: Int, flag: Boolean)

  object MyFormats1 extends DefaultFormats

  object MyFormats2 extends DefaultFormats

  object MySerializer1 extends CustomSerializer[Unit](_ => ( {
    case _ => ()
  }, {
    case _ => JNull
  }))

  object MySerializer2 extends CustomSerializer[Unit](_ => ( {
    case _ => ()
  }, {
    case _ => JNull
  }))

}
