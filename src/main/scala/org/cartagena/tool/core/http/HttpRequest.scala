package org.cartagena.tool.core.http

case class HttpRequest[T <: HttpBody](url: String,
                                      `type`: HttpMethod,
                                      headers: Map[String, String] = Map.empty,
                                      params: Map[String, String] = Map.empty,
                                      body: Option[T] = None)
