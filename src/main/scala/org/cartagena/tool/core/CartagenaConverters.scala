package org.cartagena.tool.core

import java.net.URL

object CartagenaConverters {

  implicit def stringToUrl(str: String): URL = new URL(str)

}
