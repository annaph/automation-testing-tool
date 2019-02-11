package org.cartagena.tool.suite.login

import org.cartagena.tool.core.model.{Profile, ProfileLocation}

object Profiles {

  trait LoginProfileLocation extends ProfileLocation {
    override def path: String = "/org/cartagena/tool/suite/login/"
  }

  trait LocalHostProfile extends Profile with LoginProfileLocation {
    override val name: String = "localhost-profile"

    abstract override def readProperties: Map[String, String] =
      super.readProperties ++ readPropertyFile("localhost")

  }

  trait VagrantProfile extends Profile with LoginProfileLocation {
    override val name: String = "vagrant-profile"

    abstract override def readProperties: Map[String, String] =
      super.readProperties ++ readPropertyFile("vagrant")

  }

}
