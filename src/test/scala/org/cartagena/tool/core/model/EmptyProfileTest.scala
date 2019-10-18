package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.EmptyProfileTest.MyEmptyProfile
import org.cartagena.tool.core.model.Profile.EMPTY_PROFILE_NAME
import org.scalatest.{FlatSpec, Matchers}

class EmptyProfileTest extends FlatSpec with Matchers {

  "name" should "return empty profile name" in {
    // given
    val profile = MyEmptyProfile

    // when
    val actual = profile.name

    // then
    actual should be(EMPTY_PROFILE_NAME)
  }

  "path" should "return empty profile path" in {
    // given
    val profile = MyEmptyProfile

    // when
    val actual = profile.path

    // then
    actual shouldBe empty
  }

  "readProperties" should "return empty profile property map" in {
    // given
    val profile = MyEmptyProfile

    // when
    val actual = profile.readProperties

    // then
    actual shouldBe empty
  }

}

object EmptyProfileTest {

  case object MyEmptyProfile extends EmptyProfile {

    override def readProperties: Map[String, String] =
      super.readProperties

  }

}
