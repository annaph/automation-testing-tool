package org.cartagena.tool.example.suite.login

import org.cartagena.tool.core.model.Profile

object LoginProfiles {

  trait LocalHostProfile extends Profile {

    override val name: String = "localhost-profile"

    override val path: String = "/org/cartagena/tool/example/suite/login/"

    abstract override def readProperties: Map[String, String] =
      super.readProperties ++ readPropertyFile("localhost")

  }

  trait VagrantProfile extends Profile {

    override val name: String = "vagrant-profile"

    override val path: String = "/org/cartagena/tool/example/suite/login/"

    abstract override def readProperties: Map[String, String] =
      super.readProperties ++ readPropertyFile("vagrant")

  }

}
