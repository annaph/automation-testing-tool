package org.cartagena.tool.core.http

sealed trait HttpMethod

case object HttpGet extends HttpMethod

case object HttpPost extends HttpMethod

case object UnsupportedHttpMethod$ extends HttpMethod