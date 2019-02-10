package org.cartagena.tool.core

trait Suite {

  def name: String

  def profile: Profile

  def testCases: List[TestCase]

  def setupStep: SetupStep

  def cleanupStep: CleanupStep

  def setupSteps: Stream[SetupStep] =
    Suite.getSetupSteps(this)

  def cleanupSteps: Stream[CleanupStep] =
    Suite.getCleanupSteps(this)

}

object Suite {

  private def getSetupSteps(suite: Suite): Stream[SetupStep] =
    getStepsFrom(suite.setupStep)

  private def getCleanupSteps(suite: Suite): Stream[CleanupStep] =
    getStepsFrom(suite.cleanupStep)

}
