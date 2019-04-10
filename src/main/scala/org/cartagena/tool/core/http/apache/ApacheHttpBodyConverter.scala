package org.cartagena.tool.core.http.apache

import org.apache.http.HttpEntity
import org.apache.http.entity.StringEntity
import org.cartagena.tool.core.http.json4s.inputStreamToString
import org.cartagena.tool.core.http.{EmptyBody, JsonString, Text}

sealed trait ApacheHttpBodyConverter[T] {

  def fromHttpEntity(entity: HttpEntity): T

  def toHttpEntity(body: T): HttpEntity

}

object ApacheHttpBodyConverter {

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

  implicit object EmptyBodyApacheHttpBodyConverter extends ApacheHttpBodyConverter[EmptyBody.type] {

    override def fromHttpEntity(entity: HttpEntity): EmptyBody.type =
      EmptyBody

    override def toHttpEntity(body: EmptyBody.type): HttpEntity =
      new StringEntity("")

  }

  private def convertFromStringLikeHttpEntity[T](entity: HttpEntity)(f: String => T): T =
    Some(entity)
      .map(_.getContent)
      .map(inputStreamToString)
      .map(f)
      .get

  private def convertToStringLikeHttpEntity[T](body: T)(f: T => String): StringEntity =
    new StringEntity(f(body))

}
