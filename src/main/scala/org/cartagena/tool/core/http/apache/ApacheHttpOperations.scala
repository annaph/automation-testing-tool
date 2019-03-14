package org.cartagena.tool.core.http.apache

import org.apache.http.{HeaderElement, HttpEntity, HttpResponse}
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.protocol.HttpContext
import org.cartagena.tool.core.http.Cookie
import org.cartagena.tool.core.model.HttpOperations
import java.net.{URI, URL}

trait ApacheHttpOperations extends HttpOperations {

  private[http] def addToCookieStore(elementName: String, elementValue: String, domain: String, path: String)
                                    (implicit client: HttpClient, context: HttpContext): Unit = ???

  private[http] def executePost(url: URL, entity: Option[HttpEntity], params: Map[String, String] = Map.empty)
                               (implicit client: HttpClient, context: HttpContext): HttpResponse = {
    val uriBuilder = new URIBuilder(url.toURI)

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
