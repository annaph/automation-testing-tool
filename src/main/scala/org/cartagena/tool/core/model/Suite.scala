package org.cartagena.tool.core.model

trait Suite {

  def name: String

  def profile: Profile

  def testCases: List[TestCase]

  def setupStep: SetupStep = NilStep

  def cleanupStep: CleanupStep = NilStep

  def setupSteps: Stream[SetupStep] =
    Suite.getSetupSteps(this)

  def cleanupSteps: Stream[CleanupStep] =
    Suite.getCleanupSteps(this)

}

object Suite {

  private def getSetupSteps(suite: Suite): Stream[SetupStep] =
    getStepsFrom(suite.setupStep)(_.nextSetupStep)

  private def getCleanupSteps(suite: Suite): Stream[CleanupStep] =
    getStepsFrom(suite.cleanupStep)(_.nextCleanupStep)

}
