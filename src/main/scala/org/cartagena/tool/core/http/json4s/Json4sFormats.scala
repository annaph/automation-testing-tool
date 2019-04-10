package org.cartagena.tool.core.http.json4s

import org.json4s.{DefaultFormats, Formats, Serializer}
import scalaz.effect.STRef

object Json4sFormats {

  private[json4s] val formats: STRef[Nothing, Formats] =
    STRef[Nothing](DefaultFormats)

  private[core] def getFormats: Formats = ???

  private[core] def setFormats(formats: Formats): Unit = ???

  private[core] def setDefaultFormats(): Unit = ???

  private[core] def addSerializer(serializer: Serializer[_]): Unit = ???

  private[core] def addSerializers(serializers: Traversable[Serializer[_]]): Unit = ???

}
