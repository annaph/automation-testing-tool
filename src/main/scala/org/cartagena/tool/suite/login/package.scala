package org.cartagena.tool.suite

import org.cartagena.tool.core.model.{Context, DefaultProfile, Profile, SuiteContext}
import org.cartagena.tool.suite.login.LoginProfiles.{LocalHostProfile, VagrantProfile}

package object login {

  val HEADER_ACCEPT = "headerAccept"
  val HEADER_CONTENT_TYPE = "headerContentType"
  val SESSION_COOKIE = "session_cookie"
  val LOGIN_DTO = "loginDTO"

  case class LoginSuiteContext(headerAccept: String, headerContentType: String)

  implicit val context: Context = SuiteContext(
    LoginSuiteContext(
      headerAccept = "application/json",
      headerContentType = "application/json"))

  implicit var profile: Profile = _

  val onlyVagrantProfile: Profile = new DefaultProfile with VagrantProfile

  val onlyLocalHostProfile: Profile = new DefaultProfile with LocalHostProfile

}
