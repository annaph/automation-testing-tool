package org.cartagena.tool.core.http.json4s

import org.json4s.{DefaultFormats, Formats}
import scalaz.effect.STRef

object Json4sFormatsRef {

  type FormatsRef = STRef[Nothing, Formats]

  private[json4s] val formatsRef: FormatsRef =
    STRef[Nothing](DefaultFormats)

}

