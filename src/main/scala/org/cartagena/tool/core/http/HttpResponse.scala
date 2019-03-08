package org.cartagena.tool.core.http

case class HttpResponse[T <: HttpBody](status: HttpStatus,
                                       reason: String,
                                       body: Option[T] = None,
                                       cookies: List[Cookie] = List.empty)
