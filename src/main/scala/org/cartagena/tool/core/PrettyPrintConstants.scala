package org.cartagena.tool.core

object PrettyPrintConstants {

  private[core] val NEW_LINE: String = System getProperty "line.separator"

  private[core] val TAB = "\t"

  private[core] val LINE_SEPARATOR = List.fill(97)("-").mkString("") + NEW_LINE

}
