package org.cartagena.tool.core.http.apache

import org.apache.http.client.{HttpClient => Client}
import org.apache.http.protocol.{HttpContext => Context}
import scalaz.effect.STRef

object ApacheHttpClientRefs {

  type Ref[T] = STRef[Nothing, Option[T]]

  type ClientRef = Ref[Client]

  type ContextRef = Ref[Context]

  private[apache] val clientRef: ClientRef =
    STRef[Nothing](Option.empty[Client])

  private[apache] val contextRef: ContextRef =
    STRef[Nothing](Option.empty[Context])

}
