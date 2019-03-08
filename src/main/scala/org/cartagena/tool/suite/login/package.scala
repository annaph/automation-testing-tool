package org.cartagena.tool.suite

import org.cartagena.tool.core.model.{Context, DefaultProfile, Profile, TestContext}
import org.cartagena.tool.suite.login.LoginProfiles.{LocalHostProfile, VagrantProfile}

package object login {

  implicit val context: Context[LoginSuiteContext] =
    TestContext(LoginSuiteContext())

  implicit var profile: Profile = _

  val onlyLocalHostProfile: Profile = new DefaultProfile with LocalHostProfile

  val onlyVagrantProfile: Profile = new DefaultProfile with VagrantProfile

  case class LoginSuiteContext(cookieSession: String = "")

}
