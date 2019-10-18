package org.cartagena.tool.core.http.apache

import org.apache.http.ProtocolVersion
import org.apache.http.client.protocol.HttpClientContext.COOKIE_STORE
import org.apache.http.cookie.Cookie
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.message.BasicHttpResponse
import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.http.apache.ApacheHttpTestUtil._
import org.cartagena.tool.core.registry.ApacheRestRegistryTest
import org.mockito.ArgumentMatchers.{any, same}
import org.mockito.Mockito.{verify, when}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._

class ApacheHttpOperationsTest extends FlatSpec with Matchers with ApacheRestRegistryTest {

  override private[core] val apacheHttpOperations =
    new ApacheHttpOperationsImpl

  "executeGet" should "execute HTTP GET request" in new TestNativeClientAndContext {
    // given
    var id: Long = -1L
    val response = new BasicHttpResponse(
      new ProtocolVersion(PROTOCOL, PROTOCOL_VERSION, PROTOCOL_VERSION), STATUS_CODE_200, REASON_PHRASE_OK)

    when(client.execute(any[ApacheHttpGet], same(context)))
      .thenAnswer { invocation =>
        val request = invocation getArgument(0, classOf[ApacheHttpGet])
        id = request.id

        response
      }

    // when
    val actual: ApacheHttpResponse = apacheHttpOperations executeGet(URL_STRING, Map(HEADER), Map(PARAM))

    // then
    actual should be(ApacheHttpResponse(id, response))

    verify(client).execute(any[ApacheHttpGet], same(context))
  }

  "executePost" should "execute HTTP POST request" in new TestNativeClientAndContext {
    // given
    var id: Long = -1L
    val response = new BasicHttpResponse(
      new ProtocolVersion(PROTOCOL, PROTOCOL_VERSION, PROTOCOL_VERSION), STATUS_CODE_201, REASON_PHRASE_CREATED)

    when(client.execute(any[ApacheHttpPost], same(context)))
      .thenAnswer { invocation =>
        val request = invocation getArgument(0, classOf[ApacheHttpPost])
        id = request.id

        response
      }

    // when
    val actual: ApacheHttpResponse = apacheHttpOperations executePost(
      URL_STRING, Map(HEADER), Map(PARAM), new StringEntity(BODY_CONTENT))

    // then
    actual should be(ApacheHttpResponse(id, response))

    verify(client).execute(any[ApacheHttpPost], same(context))
  }

  "executePut" should "execute HTTP PUT request" in new TestNativeClientAndContext {
    // given
    var id: Long = -1L
    val response = new BasicHttpResponse(
      new ProtocolVersion(PROTOCOL, PROTOCOL_VERSION, PROTOCOL_VERSION), STATUS_CODE_200, REASON_PHRASE_OK)

    when(client.execute(any[ApacheHttpPut], same(context)))
      .thenAnswer { invocation =>
        val request = invocation getArgument(0, classOf[ApacheHttpPut])
        id = request.id

        response
      }

    // when
    val actual: ApacheHttpResponse = apacheHttpOperations executePut(
      URL_STRING, Map(HEADER), Map(PARAM), new StringEntity(BODY_CONTENT))

    // then
    actual should be(ApacheHttpResponse(id, response))

    verify(client).execute(any[ApacheHttpPut], same(context))
  }

  "executeDelete" should "execute HTTP DELETE request" in new TestNativeClientAndContext {
    // given
    var id: Long = -1L
    val response = new BasicHttpResponse(
      new ProtocolVersion(PROTOCOL, PROTOCOL_VERSION, PROTOCOL_VERSION), STATUS_CODE_204, REASON_PHRASE_NO_CONTENT)

    when(client.execute(any[ApacheHttpDelete], same(context)))
      .thenAnswer { invocation =>
        val request = invocation getArgument(0, classOf[ApacheHttpDelete])
        id = request.id

        response
      }

    // when
    val actual: ApacheHttpResponse = apacheHttpOperations executeDelete(URL_STRING, Map(HEADER), Map(PARAM), None)

    // then
    actual should be(ApacheHttpResponse(id, response))

    verify(client).execute(any[ApacheHttpDelete], same(context))
  }

  "addToCookieStore" should "add cookie to cookie store" in new TestNativeClientAndContext {
    // given
    val cookieStore = new BasicCookieStore()

    when(context.getAttribute(COOKIE_STORE))
      .thenAnswer(_ => cookieStore)

    // when
    apacheHttpOperations addToCookieStore(COOKIE_NAME, COOKIE_VALUE, COOKIE_DOMAIN, COOKIE_PATH)

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
