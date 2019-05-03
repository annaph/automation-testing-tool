package org.cartagena.tool.core.http.json4s

import org.cartagena.tool.core.http.json4s.Json4sFormats._
import org.cartagena.tool.core.http.json4s.Json4sFormatsTest._
import org.json4s.JsonAST.JNull
import org.json4s.{CustomSerializer, DefaultFormats}
import org.scalatest.{FlatSpec, Matchers}
import scalaz.effect.STRef

class Json4sFormatsTest extends FlatSpec with Matchers with Json4sFormatsWithMyDefaultFormats {

  "formats" should "return configured Json4s Formats" in {
    // then
    formats shouldBe a[MyDefaultFormats.type]
    formats.customSerializers should have size 0
  }

  "setFormats" should "set new Json4s Formats" in {
    // given
    val newFormats = MyDefaultFormats2

    // when
    setFormats(formatsRef, newFormats)

    // then
    formats shouldBe a[MyDefaultFormats2.type]
    formats.customSerializers should have size 0
  }

  "addSerializers" should "add custom serializers to Json4s Formats" in {
    // given
    val serializers = List(MyCustomSerializer, MyCustomSerializer2)

    // when
    addSerializers(formatsRef, serializers)
    val customSerializers = formats.customSerializers

    // then
    customSerializers should have size 2
    customSerializers.head shouldBe a[MyCustomSerializer.type]
    customSerializers.tail.head shouldBe a[MyCustomSerializer2.type]
  }

}

object Json4sFormatsTest {

  trait Json4sFormatsWithMyDefaultFormats extends Json4sFormats {

    override private[json4s] val formatsRef =
      STRef[Nothing](MyDefaultFormats)

  }

  object MyDefaultFormats extends DefaultFormats

  object MyDefaultFormats2 extends DefaultFormats

  object MyCustomSerializer extends CustomSerializer[Unit](_ => ( {
    case _ => ()
  }, {
    case _ => JNull
  }))

  object MyCustomSerializer2 extends CustomSerializer[Unit](_ => ( {
    case _ => ()
  }, {
    case _ => JNull
  }))

}
