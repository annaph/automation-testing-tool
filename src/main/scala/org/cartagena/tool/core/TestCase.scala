package org.cartagena.tool.core

trait TestCase {

  val name: String

  def profile: Profile

  def setupStep: SetupStep

  def testStep: TestStep

  def cleanupStep: CleanupStep

  def getSteps: Stream[Step] =
    TestCase getSteps this

}

object TestCase {

  def getSteps[T](suite: TestCase): Stream[Step] = {
    def getNextStep(currStep: Step): Option[Step] = currStep.nextStep match {
      case Some(step) =>
        Some(step)
      case None => currStep match {
        case _: SetupStep =>
          Some(suite.testStep)
        case _: TestStep =>
          Some(suite.cleanupStep)
        case _: CleanupStep =>
          None
      }
    }

    def go(currStep: Step): Stream[Step] = {
      getNextStep(currStep) match {
        case Some(nextStep) =>
          currStep #:: go(nextStep)
        case None =>
          currStep #:: Stream.empty[Step]
      }
    }

    go(suite.setupStep)
  }

}
