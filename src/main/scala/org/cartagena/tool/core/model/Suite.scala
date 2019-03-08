package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process._

trait Suite {

  def name: String

  def testCases: List[TestCase]

  def setupStep: SetupStep = NilStep

  def cleanupStep: CleanupStep = NilStep

  def setupSteps: Stream[SetupStep] =
    Suite getSetupSteps this

  def cleanupSteps: Stream[CleanupStep] =
    Suite getCleanupSteps this

  def run(): Unit =
    Suite run this

}

object Suite {

  private def getSetupSteps(suite: Suite): Stream[SetupStep] =
    getStepsFrom(suite.setupStep)(_.nextSetupStep)

  private def getCleanupSteps(suite: Suite): Stream[CleanupStep] =
    getStepsFrom(suite.cleanupStep)(_.nextCleanupStep)

  private def run(suite: Suite): Unit = {
    val process = lift[TestStep, Unit](step => {
      println(s"\nExecuting: ${step.name}\n")
      step.run()
    })

    suite.testCases.foreach(testCase => {
      process(testCase.testSteps).toList
    })
  }

}
