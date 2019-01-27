package org.cartagena.tool.core

import java.io.File

trait Profile {
  def name: String

  def propertyFile: File

  def readProperties: Map[String, Any]
}

class Context[T](value: T) {
  def getContext: T = value
}

abstract class Suite[T](profile: Profile, context: Context[T]) {
  def name: String
}