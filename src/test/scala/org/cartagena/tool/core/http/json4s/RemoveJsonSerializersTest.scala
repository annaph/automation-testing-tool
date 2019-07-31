package org.cartagena.tool.core.http.json4s

import org.cartagena.tool.core.http.JsonHelper
import org.cartagena.tool.core.http.json4s.RemoveJsonSerializers.STEP_NAME
import org.cartagena.tool.core.registry.Json4sRegistryTest
import org.json4s.DefaultFormats
import org.mockito.Mockito.{doNothing, verify}
import org.scalatest.{FlatSpec, Matchers}

class RemoveJsonSerializersTest extends FlatSpec with Matchers with Json4sRegistryTest {

  "name" should "return step name" in {
    // given
    val step = RemoveJsonSerializers()

    // when
    val actual = step.name

    // then
    actual should be(STEP_NAME)
  }

  "run" should "run Remove Json serializers step" in {
    // given
    val step = new RemoveJsonSerializers() {
      override val jsonHelper: JsonHelper = RemoveJsonSerializersTest.this.json4sHelper
    }

    val formats = DefaultFormats

    doNothing().when(step.jsonHelper).useJsonFormats(formats)

    // when
    step.run()

    // then
    verify(step.jsonHelper).useJsonFormats(formats)
  }

}
