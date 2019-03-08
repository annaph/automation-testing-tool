package org.cartagena.tool.core.http.apache

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.apache.http.HttpEntity
import org.apache.http.entity.StringEntity
import org.cartagena.tool.core.http.{EmptyBody, JsonString, Text}

sealed trait ApacheBodyConverter[T] {

  def fromHttpEntity(entity: HttpEntity): T

  def toHttpEntity(body: T): HttpEntity

}

object ApacheBodyConverter {

  implicit object TextApacheBodyConverter extends ApacheBodyConverter[Text] {

    override def fromHttpEntity(entity: HttpEntity): Text =
      convertFromStringLikeHttpEntity(entity)(Text)

    override def toHttpEntity(body: Text): HttpEntity =
      convertToStringLikeHttpEntity(body)(_.str)
  }

  implicit object JsonStringApacheBodyConverter extends ApacheBodyConverter[JsonString] {

    override def fromHttpEntity(entity: HttpEntity): JsonString =
      convertFromStringLikeHttpEntity(entity)(JsonString)

    override def toHttpEntity(body: JsonString): HttpEntity =
      convertToStringLikeHttpEntity(body)(_.str)

  }

  implicit object EmptyBodyApacheBodyConverter extends ApacheBodyConverter[EmptyBody.type] {

    override def fromHttpEntity(entity: HttpEntity): EmptyBody.type =
      EmptyBody

    override def toHttpEntity(body: EmptyBody.type): HttpEntity =
      new StringEntity("")

  }

  private def convertFromStringLikeHttpEntity[T](entity: HttpEntity)(f: String => T): T =
    Some(entity)
      .map(_.getContent)
      .map(IOUtils toString(_, StandardCharsets.UTF_8.name()))
      .map(f)
      .get

  private def convertToStringLikeHttpEntity[T](body: T)(f: T => String): StringEntity =
    new StringEntity(f(body))

}
