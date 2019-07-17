package org.cartagena.tool.core

import java.io.InputStream
import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils

package object http {

  implicit def inputStreamToString(in: InputStream): String =
    IOUtils toString(in, StandardCharsets.UTF_8.name())

}
