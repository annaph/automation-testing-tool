package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.DefaultProfileTest.MyDefaultProfile
import org.cartagena.tool.core.model.Profile.DEFAULT_PROFILE_NAME
import org.scalatest.{FlatSpec, Matchers}

class DefaultProfileTest extends FlatSpec with Matchers {

  "name" should "return default profile name" in {
    // given
    val profile = MyDefaultProfile

    // when
    val actual = profile.name

    // then
    actual should be(DEFAULT_PROFILE_NAME)
  }

  "readProperties" should "read default profile properties from 'default.property' file" in {
    // given
    val profile = MyDefaultProfile

    val expected = Map("key" -> "value")

    // when
    val actual = profile.readProperties

    // then
    actual should be(expected)
  }

}

object DefaultProfileTest {

  case object MyDefaultProfile extends DefaultProfile {

    override def path: String = "/org/cartagena/tool/core/model/"

    override def readProperties: Map[String, String] =
      super.readProperties

  }

}
