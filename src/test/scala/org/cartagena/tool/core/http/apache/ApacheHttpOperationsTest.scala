package org.cartagena.tool.core.http.apache

import org.apache.http.ProtocolVersion
import org.apache.http.client.HttpClient
import org.apache.http.client.protocol.HttpClientContext.COOKIE_STORE
import org.apache.http.cookie.Cookie
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.message.BasicHttpResponse
import org.apache.http.protocol.HttpContext
import org.cartagena.tool.core.CartagenaUtils._
import org.mockito.ArgumentMatchers.{any, same}
import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._

class ApacheHttpOperationsTest extends FlatSpec with Matchers {

  import ApacheHttpOperationsTest._

  private val PROTOCOL = "HTTP"
  private val VERSION = 1
  private val STATUS_CODE = 7
  private val REASON_STRING = "Great"

  private val URL_STRING = "http://www.google.com"
  private val HEADER = "header1" -> "value1"
  private val PARAM = "param1" -> "value1"
  private val BODY_CONTENT = "This is some body"

  private val COOKIE_NAME = "name"
  private val COOKIE_VALUE = "value"
  private val COOKIE_DOMAIN = "domain"
  private val COOKIE_PATH = "path"

  "executeGet" should "execute HTTP GET request" in new TestEnvironment {
    // given
    var id: Long = -1
    val response = new BasicHttpResponse(new ProtocolVersion(PROTOCOL, VERSION, VERSION), STATUS_CODE, REASON_STRING)

    when(client.execute(any[ApacheHttpGet], same(context)))
      .thenAnswer { invocation =>
        val request = invocation getArgument(0, classOf[ApacheHttpGet])
        id = request.id

        response
      }

    // when
    val actual: ApacheHttpResponse = executeGet(URL_STRING, Map(HEADER), Map(PARAM))

    // then
    actual should be(ApacheHttpResponse(id, response))

    verify(client).execute(any[ApacheHttpGet], same(context))
  }

  "executePost" should "execute HTTP POST request" in new TestEnvironment {
    // given
    var id: Long = -1
    val response = new BasicHttpResponse(new ProtocolVersion(PROTOCOL, VERSION, VERSION), STATUS_CODE, REASON_STRING)

    when(client.execute(any[ApacheHttpPost], same(context)))
      .thenAnswer { invocation =>
        val request = invocation getArgument(0, classOf[ApacheHttpPost])
        id = request.id

        response
      }

    // when
    val actual: ApacheHttpResponse = executePost(URL_STRING, new StringEntity(BODY_CONTENT), Map(HEADER), Map(PARAM))

    // then
    actual should be(ApacheHttpResponse(id, response))

    verify(client).execute(any[ApacheHttpPost], same(context))
  }

  "addToCookieStore" should "add cookie to cookie store" in new TestEnvironment {
    // given
    val cookieStore = new BasicCookieStore()

    when(context.getAttribute(COOKIE_STORE))
      .thenAnswer(_ => cookieStore)

    // when
    addToCookieStore(COOKIE_NAME, COOKIE_VALUE, COOKIE_DOMAIN, COOKIE_PATH)

    val cookies: List[Cookie] = cookieStore.getCookies.asScala.toList
    val storedCookie: Cookie = cookies.head

    // then
    cookies should have size 1
    storedCookie.getName should be(COOKIE_NAME)
    storedCookie.getValue should be(COOKIE_VALUE)
    storedCookie.getDomain should be(COOKIE_DOMAIN)
    storedCookie.getPath should be(COOKIE_PATH)

    verify(context).getAttribute(COOKIE_STORE)
  }

}

object ApacheHttpOperationsTest {

  trait TestEnvironment extends ApacheHttpOperations with MockitoSugar {

    implicit val client: HttpClient = mock[HttpClient]

    implicit val context: HttpContext = mock[HttpContext]

  }

}
