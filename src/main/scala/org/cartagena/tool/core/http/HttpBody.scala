package org.cartagena.tool.core.http

sealed trait HttpBody extends Any

case class Text(str: String) extends AnyVal with HttpBody

case class JsonString(str: String) extends AnyVal with HttpBody

case object EmptyBody extends HttpBody
