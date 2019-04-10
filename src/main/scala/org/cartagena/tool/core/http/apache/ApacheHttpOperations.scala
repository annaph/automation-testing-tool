package org.cartagena.tool.core.http.apache

import java.net.URL
import java.util.concurrent.atomic.AtomicLong

import org.apache.http.HttpEntity
import org.apache.http.client.HttpClient
import org.apache.http.client.protocol.HttpClientContext.COOKIE_STORE
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.protocol.HttpContext
import org.cartagena.tool.core.model.HttpOperations

trait ApacheHttpOperations extends HttpOperations {

  import ApacheHttpOperations._

  private[apache] def executeGet(url: URL, headers: NameValuePairs, params: NameValuePairs)
                                (implicit client: HttpClient, context: HttpContext): ApacheHttpResponse =
    ApacheHttpOperations.executeGet(url, headers, params)

  private[apache] def executePost(url: URL, entity: HttpEntity, headers: NameValuePairs, params: NameValuePairs)
                                 (implicit client: HttpClient, context: HttpContext): ApacheHttpResponse =
    ApacheHttpOperations.executePost(url, entity, headers, params)

  private[apache] def addToCookieStore(elementName: String, elementValue: String, domain: String, path: String)
                                      (implicit client: HttpClient, context: HttpContext): Unit =
    ApacheHttpOperations.addToCookieStore(elementName, elementValue, domain, path)

}

object ApacheHttpOperations {

  type NameValuePairs = Map[String, String]

  private val COUNTER_INIT_VALUE = 0

  private val idCounter = new AtomicLong(COUNTER_INIT_VALUE)

  private def executeGet(url: URL, headers: NameValuePairs, params: NameValuePairs)
                        (implicit client: HttpClient, context: HttpContext): ApacheHttpResponse =
    execute { id =>
      ApacheHttpRequestBuilder[CannotHaveEntity]()
        .withId(id)
        .withURL(url)
        .withHeaders(headers)
        .withParams(params)
        .buildHttpGet()
    }

  private def executePost(url: URL, entity: HttpEntity, headers: NameValuePairs, params: NameValuePairs)
                         (implicit client: HttpClient, context: HttpContext): ApacheHttpResponse =
    execute { id =>
      ApacheHttpRequestBuilder[MustHaveEntity]()
        .withId(id)
        .withURL(url)
        .withHeaders(headers)
        .withParams(params)
        .withEntity(entity)
        .buildHttpPost()
    }

  private def execute[T <: ApacheHttpRequest](request: Long => T)
                                             (implicit client: HttpClient, context: HttpContext): ApacheHttpResponse = {
    val id = ApacheHttpOperations.idCounter.incrementAndGet()
    ApacheHttpResponse(id, client execute(request(id), context))
  }

  private def addToCookieStore(elementName: String, elementValue: String, domain: String, path: String)
                              (implicit client: HttpClient, context: HttpContext): Unit = {
    val cookie = new BasicClientCookie(elementName, elementValue)
    cookie setDomain domain
    cookie setPath path

    val cookieStore = (context getAttribute COOKIE_STORE).asInstanceOf[BasicCookieStore]
    cookieStore addCookie cookie
  }

}
