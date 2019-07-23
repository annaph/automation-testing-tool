package org.cartagena.tool.core.http.json4s

import org.json4s.{CustomSerializer, DefaultFormats}
import org.json4s.JsonAST.JNull
import scalaz.effect.STRef

object Json4sTestUtil {

  class Json4sClientWithMyDefaultFormats extends Json4sClientImpl {

    override private[json4s] val formatsRef =
      STRef[Nothing](MyDefaultFormats1)

  }

  object MyDefaultFormats1 extends DefaultFormats

  object MyDefaultFormats2 extends DefaultFormats

  object MyCustomSerializer1 extends CustomSerializer[Unit](_ => ( {
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
