package org.cartagena.tool.core.http.apache

import java.net.{URI, URL}

import org.apache.http.HttpEntity
import org.apache.http.client.utils.URIBuilder

sealed trait BuildStep

sealed trait HasID extends BuildStep

sealed trait HasURL extends BuildStep

sealed trait HasHeaders extends BuildStep

sealed trait HasParams extends BuildStep

sealed trait EntityMark

sealed trait CanHaveEntity extends EntityMark

sealed trait CannotHaveEntity extends EntityMark

class ApacheHttpRequestBuilder[S <: BuildStep, E <: EntityMark] private(var id: Long,
                                                                        var url: URL,
                                                                        var headers: Map[String, String],
                                                                        var params: Map[String, String],
                                                                        var entity: Option[HttpEntity] = None) {
  def withId(id: Long): ApacheHttpRequestBuilder[HasID, E] = {
    this.id = id
    new ApacheHttpRequestBuilder[HasID, E](this)
  }

  protected def this(builder: ApacheHttpRequestBuilder[_, _]) =
    this(builder.id, builder.url, builder.headers, builder.params, builder.entity)

  def withURL(url: URL)(implicit ev: S =:= HasID): ApacheHttpRequestBuilder[HasURL, E] = {
    this.url = url
    new ApacheHttpRequestBuilder[HasURL, E](this)
  }

  def withHeaders(headers: Map[String, String])(implicit ev: S =:= HasURL): ApacheHttpRequestBuilder[HasHeaders, E] = {
    this.headers = headers
    new ApacheHttpRequestBuilder[HasHeaders, E](this)
  }

  def withParams(params: Map[String, String])(implicit ev: S =:= HasHeaders): ApacheHttpRequestBuilder[HasParams, E] = {
    this.params = params
    new ApacheHttpRequestBuilder[HasParams, E](this)
  }

  def withEntity(entity: HttpEntity)(implicit ev: E =:= CanHaveEntity): ApacheHttpRequestBuilder[S, E] = {
    this.entity = Some(entity)
    new ApacheHttpRequestBuilder[S, E](this)
  }

  def buildHttpGet()(implicit ev1: S =:= HasParams, ev2: E =:= CannotHaveEntity): ApacheHttpGetRequest =
    ApacheHttpRequestBuilder.buildHttpRequest(id, url, headers, params, entity)(new ApacheHttpGetRequest(_, _))

  def buildHttpPost()(implicit ev1: S =:= HasParams, ev2: E =:= CanHaveEntity): ApacheHttpPostRequest =
    ApacheHttpRequestBuilder.buildHttpRequest(id, url, headers, params, entity)(new ApacheHttpPostRequest(_, _))

  protected def this() =
    this(0, null, Map.empty, Map.empty)

}

object ApacheHttpRequestBuilder {

  def apply[E <: EntityMark](): ApacheHttpRequestBuilder[BuildStep, E] =
    new ApacheHttpRequestBuilder[BuildStep, E]()

  private def buildHttpRequest[T <: ApacheHttpRequest](id: Long,
                                                       url: URL,
                                                       headers: Map[String, String],
                                                       params: Map[String, String],
                                                       entity: Option[HttpEntity])
                                                      (f: (Long, URI) => T): T = {
    val uriBuilder = new URIBuilder(url.toURI)

    params.foreach {
      case (name, value) =>
        uriBuilder setParameter(name, value)
    }

    val request = f(id, uriBuilder.build())

    headers.foreach {
      case (name, value) =>
        request addHeader(name, value)
    }

    entity match {
      case Some(ent) =>
        request setEntity ent
      case _ =>
    }

    request
  }

}
