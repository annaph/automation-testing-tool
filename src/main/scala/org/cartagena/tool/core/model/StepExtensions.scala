package org.cartagena.tool.core.model

object StepExtensions {

  trait InfoMessages {
    self: Step =>

    def preRunMsg: String =
      s"Executing '$name'..."

    def passedRunMsg: String =
      s"Finish executing '$name' with success."

    def failedRunMsg: String =
      s"Finish executing '$name' with failure!"

    def ignoredRunMsg: String =
      s"Execution of '$name' ignored!"

  }

  trait ProfileAndContext {
    self: Step =>

    def profile: Profile =
      EmptyProfile

    def context: ContextX =
      EmptyContextX

  }

  trait UnsupportedRunnable {
    self: Step =>

    def run(): Unit =
      throw new UnsupportedOperationException

  }

}
