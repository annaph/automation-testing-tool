package org.cartagena.tool.core.http.apache

import org.apache.http.client.HttpClient
import org.apache.http.protocol.HttpContext
import org.scalatestplus.mockito.MockitoSugar

trait TestNativeClientAndContext extends MockitoSugar {

  implicit val client: HttpClient = mock[HttpClient]

  implicit val context: HttpContext = mock[HttpContext]

}
