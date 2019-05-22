package org.cartagena.tool.core.model

object StepExtensions {

  trait InfoMessages {
    self: StepX =>

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
    self: StepX =>

    def profile: Profile =
      EmptyProfile

    def context: Context =
      EmptyContext

  }

  trait UnsupportedRunnable {
    self: StepX =>

    def run(): Unit =
      throw new UnsupportedOperationException

  }

}
