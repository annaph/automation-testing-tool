package org.cartagena.tool.suite

import org.cartagena.tool.core.model.{Context, DefaultProfile, Profile, TestContext}
import org.cartagena.tool.suite.login.Profiles.{LocalHostProfile, VagrantProfile}

package object login {

  implicit val context: Context[LoginSuiteContext] =
    TestContext(LoginSuiteContext())

  case class LoginSuiteContext(cookieSession: String = "")

  implicit val onlyLocalHostProfile: Profile = new DefaultProfile with LocalHostProfile

  implicit val onlyVagrantProfile: Profile = new DefaultProfile with VagrantProfile

}
