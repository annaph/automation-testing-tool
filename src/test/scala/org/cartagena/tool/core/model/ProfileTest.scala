package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.ProfileTest._
import org.scalatest.{FlatSpec, Matchers}

class ProfileTest extends FlatSpec with Matchers {

  "getProperty" should "return the value of the profile property associated with a given key" in {
    // given
    val profile = MyProfile
    val key = PROP_1._1

    val expected = Some(PROP_1._2)

    // when
    val actual = profile getProperty key

    // then
    actual should be(expected)
  }

  it should "return an empty value when given key is not associated with any profile property" in {
    // given
    val profile = MyProfile
    val key = "non-existing"

    // when
    val actual = profile getProperty key

    // then
    actual shouldBe empty
  }

  "getAllProperties" should "return all profile properties" in {
    // given
    val profile = MyProfile

    val expected = Map(PROP_1, PROP_2, PROP_3)

    // when
    val actual = profile.getAllProperties

    // then
    actual should be(expected)
  }

  it should "read profile properties from property file" in {
    // given
    val profile = MyProfileWithPropertyFile

    val expected = Map(PROP_1, PROP_2, PROP_3)

    // when
    val actual = profile.getAllProperties

    // then
    actual should be(expected)
  }

  it should "throw exception when reading profile properties from non-existing property file" in {
    // given
    val profile = MyProfileWithNonExistingPropertyFile

    intercept[Exception] {
      // when
      profile.getAllProperties
    }
  }

}

object ProfileTest {

  val PROP_1: (String, String) = "key1" -> "value1"

  val PROP_2: (String, String) = "key2" -> "value2"

  val PROP_3: (String, String) = "key3" -> "value3"

  case object MyProfile extends Profile {

    override val name: String = "my-profile"

    override val path: String = ""

    override protected def readProperties: Map[String, String] =
      Map(PROP_1, PROP_2, PROP_3)

  }

  case object MyProfileWithPropertyFile extends Profile {

    override val name: String = "my-profile-with-property-file"

    override val path: String = "/org/cartagena/tool/core/model/"

    override protected def readProperties: Map[String, String] =
      readPropertyFile("my-profile")

  }

  case object MyProfileWithNonExistingPropertyFile extends Profile {

    override val name: String = "my-profile-with-non-existing-property-file"

    override val path: String = "/org/cartagena/tool/core/model/"

    override protected def readProperties: Map[String, String] =
      readPropertyFile("non-existing")

  }

}
