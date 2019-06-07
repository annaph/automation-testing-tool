package org.cartagena.tool.core.http

sealed trait NameValuePair {

  def name: String

  def value: String

}

case class HeaderElement(name: String, value: String) extends NameValuePair

case class QueryParam(name: String, value: String) extends NameValuePair

case class Cookie(name: String, value: String, host: String, path: String) extends NameValuePair
