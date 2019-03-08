package org.cartagena.tool.core.http.apache

import org.apache.http.{HttpEntity, HttpResponse}
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.protocol.HttpContext
import org.cartagena.tool.core.model.RestOperations

trait ApacheRestOperations extends RestOperations {

  private[http] def executePost(url: String, entity: Option[HttpEntity], params: Map[String, String] = Map.empty)
                               (implicit client: HttpClient, context: HttpContext): HttpResponse = {
    val uriBuilder = new URIBuilder(url)

    params.foreach {
      case (name, value) =>
        uriBuilder setParameter(name, value)
    }

    val request = new HttpPost(uriBuilder.build())

    entity match {
      case Some(ent) =>
        request setEntity ent
      case None =>
    }

    client execute(request, context)
  }

}
