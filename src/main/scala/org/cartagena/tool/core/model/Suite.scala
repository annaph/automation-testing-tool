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
    val setupStepPocess = lift[SetupStep, String](step => {
      println(s"\nExecuting: ${step.name}\n")
      step.run()

      step.name
    })

    val testStepPocess = lift[TestStep, String](step => {
      println(s"\nExecuting: ${step.name}\n")
      step.run()

      step.name
    })

    val cleanupStepPocess = lift[CleanupStep, String](step => {
      println(s"\nExecuting: ${step.name}\n")
      step.run()

      step.name
    })

    setupStepPocess(getSetupSteps(suite)).toList

    suite.testCases.foreach(testCase => {
      val executedSteps = testStepPocess(testCase.testSteps).toList
      println(s"Test case execution report: $executedSteps")
    })

    cleanupStepPocess(getCleanupSteps(suite)).toList
  }

}
