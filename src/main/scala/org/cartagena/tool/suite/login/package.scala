package org.cartagena.tool.suite

import org.cartagena.tool.core.model.{Context, DefaultProfile, Profile, SuiteContext}
import org.cartagena.tool.suite.login.LoginProfiles.{LocalHostProfile, VagrantProfile}

package object login {

  val HEADER_ACCEPT = "headerAccept"
  val HEADER_CONTENT_TYPE = "headerContentType"
  val LOGIN_RESPONSE = "loginResponse"

  implicit val context: Context = SuiteContext(
    LoginSuiteContext(
      headerAccept = "application/json",
      headerContentType = "application/json"))

  val onlyVagrantProfile: Profile = new DefaultProfile with VagrantProfile

  val onlyLocalHostProfile: Profile = new DefaultProfile with LocalHostProfile

  implicit var profile: Profile = _

  case class LoginSuiteContext(headerAccept: String, headerContentType: String)

}
