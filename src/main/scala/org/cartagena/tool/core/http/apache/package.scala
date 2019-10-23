package org.cartagena.tool.core.http

import java.io.{BufferedReader, InputStream, InputStreamReader}
import java.nio.charset.StandardCharsets

import org.apache.http.HttpEntity

import scala.jdk.StreamConverters._

package object apache {

  implicit def intToHttpStatus(code: Int): HttpStatus =
    HttpStatus(code)

  implicit def inputStreamToString(in: InputStream): String = {
    val reader = new BufferedReader(
      new InputStreamReader(in, StandardCharsets.UTF_8.name()))

    reader.lines().toScala(Iterator) mkString System.lineSeparator()
  }

  implicit def nameValuePairsToMap(nameValuePairs: List[NameValuePair]): Map[String, String] =
    nameValuePairs.map {
      case NameValuePair(name, value) =>
        name -> value
    }.toMap


  implicit def httpBodyToHttpEntity[T <: HttpBody](body: T): HttpEntity =
    body match {
      case x: Text =>
        toEntity[Text](x)
      case x: JsonString =>
        toEntity[JsonString](x)
      case x: Empty.type =>
        toEntity[Empty.type](x)
    }

  implicit def httpBodyToHttpEntityOption[T <: HttpBody](body: T): Option[HttpEntity] =
    body match {
      case Empty =>
        None
      case x =>
        Some(x)
    }

  def toCookies(nameValuePairs: List[NameValuePair], host: String, path: String): List[Cookie] =
    nameValuePairs.map {
      case NameValuePair(name, value) =>
        Cookie(name, value, host, path)
    }

  private def toEntity[T <: HttpBody : ApacheHttpBodyConverter](body: T): HttpEntity =
    implicitly[ApacheHttpBodyConverter[T]].toHttpEntity(body)

}
