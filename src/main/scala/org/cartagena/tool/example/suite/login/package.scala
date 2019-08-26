package org.cartagena.tool.example.suite

import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.model.StepExtensions.ProfileAndContext
import org.cartagena.tool.core.model._
import org.cartagena.tool.example.suite.login.LoginProfiles.{LocalHostProfile, VagrantProfile}

package object login {

  val HEADER_ACCEPT = "headerAccept"
  val HEADER_CONTENT_TYPE = "headerContentType"
  val LOGIN_RESPONSE = "loginResponse"

  val loginContext: Context = SuiteContext()

  loginContext ~=> HEADER_ACCEPT />[String] "application/json"
  loginContext ~=> HEADER_CONTENT_TYPE />[String] "application/json"

  val onlyVagrantProfile: Profile = new DefaultProfile with VagrantProfile
  val onlyLocalHostProfile: Profile = new DefaultProfile with LocalHostProfile

  var loginProfile: Profile = _

  trait LoginProfileAndContext extends ProfileAndContext {
    self: Step =>

    override def profile: Profile =
      loginProfile

    override def context: Context =
      loginContext

  }

  case class LoginSuiteContext(headerAccept: String, headerContentType: String)

}
