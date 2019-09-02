package org.cartagena.tool.core.http.apache

import org.apache.http.HttpEntity
import org.apache.http.entity.StringEntity
import org.cartagena.tool.core.http.{Empty, HttpBody, JsonString, Text}

sealed trait ApacheHttpBodyConverter[T <: HttpBody] {

  def fromHttpEntity(entity: HttpEntity): T

  def toHttpEntity(body: T): HttpEntity

}

object ApacheHttpBodyConverter {

  private[http] val EMPTY_STRING = ""

  implicit object TextApacheHttpBodyConverter extends ApacheHttpBodyConverter[Text] {

    override def fromHttpEntity(entity: HttpEntity): Text =
      convertFromStringLikeHttpEntity(entity)(Text)

    override def toHttpEntity(body: Text): HttpEntity =
      convertToStringLikeHttpEntity(body)(_.str)
  }

  implicit object JsonStringApacheHttpBodyConverter extends ApacheHttpBodyConverter[JsonString] {

    override def fromHttpEntity(entity: HttpEntity): JsonString =
      convertFromStringLikeHttpEntity(entity)(JsonString)

    override def toHttpEntity(body: JsonString): HttpEntity =
      convertToStringLikeHttpEntity(body)(_.str)

  }

  implicit object EmptyApacheHttpBodyConverter extends ApacheHttpBodyConverter[Empty.type] {

    override def fromHttpEntity(entity: HttpEntity): Empty.type =
      Empty

    override def toHttpEntity(body: Empty.type): HttpEntity =
      new StringEntity(EMPTY_STRING)

  }

  private def convertFromStringLikeHttpEntity[T](entity: HttpEntity)(f: String => T): T =
    f(entity.getContent)

  private def convertToStringLikeHttpEntity[T <: HttpBody](body: T)(f: T => String): StringEntity =
    new StringEntity(f(body))

}
