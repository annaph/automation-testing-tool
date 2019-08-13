package org.cartagena.tool.core.http.apache

import org.apache.http.message.BasicHttpResponse
import org.apache.http.{HttpEntity, ProtocolVersion}
import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.http._
import org.cartagena.tool.core.http.apache.ApacheHttpTestUtil._
import org.cartagena.tool.core.registry.ApacheRestRegistryTest
import org.mockito.ArgumentMatchers.{any, same, eq => eqTo}
import org.mockito.Mockito.{doNothing, verify, when}
import org.scalatest.{FlatSpec, Matchers}

class ApacheRestHelperTest extends FlatSpec with Matchers with ApacheRestRegistryTest {

  override private[core] val apacheHttpRestHelper =
    new ApacheRestHelperImpl(apacheHttpClient, apacheHttpOperations)

  "startRestClient" should "start Rest client" in {
    // given
    doNothing().when(apacheHttpClient).start()

    // when
    apacheHttpRestHelper.startRestClient()

    // then
    verify(apacheHttpClient).start()
  }

  "restartRestClient" should "rest Rest client" in {
    // given
    doNothing().when(apacheHttpClient).reset()

    // when
    apacheHttpRestHelper.restartRestClient()

    // then
    verify(apacheHttpClient).reset()
  }

  "shutdownRestClient" should "shutdown Rest client" in {
    // given
    doNothing().when(apacheHttpClient).close()

    // when
    apacheHttpRestHelper.shutdownRestClient()

    // then
    verify(apacheHttpClient).close()
  }

  "isRestClientRunning" should "check is Rest client running" in {
    // given
    when(apacheHttpClient.isUp)
      .thenReturn(true)

    // when
    val actual = apacheHttpRestHelper.isRestClientRunning

    // then
    actual should be(true)

    verify(apacheHttpClient).isUp
  }

  "execute" should "execute HTTP GET request" in new TestNativeClientAndContext {
    // given
    val request = HttpRequest(
      url = URL_STRING,
      method = Get,
      body = EmptyBody)

    when(apacheHttpClient.get)
      .thenReturn(client)

    when(apacheHttpClient.context)
      .thenReturn(context)

    when(apacheHttpOperations
      .executeGet(eqTo(stringToUrl(URL_STRING)), eqTo(Map.empty), eqTo(Map.empty))(same(client), same(context)))
      .thenReturn(
        new ApacheHttpResponse(
          1L,
          new BasicHttpResponse(
            new ProtocolVersion(PROTOCOL, PROTOCOL_VERSION, PROTOCOL_VERSION), STATUS_CODE_200, REASON_PHRASE_OK)))

    val expected = HttpResponse(OK, REASON_PHRASE_OK, EmptyBody)

    // when
    val actual: HttpResponse[EmptyBody.type] = apacheHttpRestHelper execute request

    // then
    actual should be(expected)

    verify(apacheHttpOperations)
      .executeGet(eqTo(stringToUrl(URL_STRING)), eqTo(Map.empty), eqTo(Map.empty))(same(client), same(context))
  }

  it should "execute HTTP POST request" in new TestNativeClientAndContext {
    // given
    val request = HttpRequest(
      url = URL_STRING,
      method = Post,
      body = EmptyBody)

    when(apacheHttpClient.get)
      .thenReturn(client)

    when(apacheHttpClient.context)
      .thenReturn(context)

    when(apacheHttpOperations
      .executePost(
        eqTo(stringToUrl(URL_STRING)), any[HttpEntity], eqTo(Map.empty), eqTo(Map.empty))(same(client), same(context)))
      .thenReturn(
        new ApacheHttpResponse(
          1L,
          new BasicHttpResponse(
            new ProtocolVersion(PROTOCOL, PROTOCOL_VERSION, PROTOCOL_VERSION), STATUS_CODE_201, REASON_PHRASE_CREATED)))

    val expected = HttpResponse(Created, REASON_PHRASE_CREATED, EmptyBody)

    // when
    val actual: HttpResponse[EmptyBody.type] = apacheHttpRestHelper execute request

    // then
    actual should be(expected)

    verify(apacheHttpOperations)
      .executePost(
        eqTo(stringToUrl(URL_STRING)), any[HttpEntity], eqTo(Map.empty), eqTo(Map.empty))(same(client), same(context))

  }

  "storeCookie" should "store Cookie" in new TestNativeClientAndContext {
    // given
    val cookie = Cookie(COOKIE_NAME, COOKIE_VALUE, COOKIE_DOMAIN, COOKIE_PATH)

    when(apacheHttpClient.get)
      .thenReturn(client)

    when(apacheHttpClient.context)
      .thenReturn(context)

    doNothing().when(apacheHttpOperations)
      .addToCookieStore(COOKIE_NAME, COOKIE_VALUE, COOKIE_DOMAIN, COOKIE_PATH)(client, context)

    // when
    apacheHttpRestHelper storeCookie cookie

    // then
    verify(apacheHttpOperations)
      .addToCookieStore(COOKIE_NAME, COOKIE_VALUE, COOKIE_DOMAIN, COOKIE_PATH)(client, context)
  }

}
