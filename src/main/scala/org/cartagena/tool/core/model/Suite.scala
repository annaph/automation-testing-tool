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

  private def run(suite: Suite): Unit = {
    def printStepSeparator(): Unit =
      println(StringBuilder.newBuilder.append("-") * 71)

    val setupStepProcess = lift[SetupStep, String](step => {
      printStepSeparator()
      println(s"\nExecuting '${step.name}' setup step...\n")

      step.run()
      printStepSeparator()

      step.name
    })

    val testStepProcess = lift[TestStep, String](step => {
      printStepSeparator()
      println(s"\nExecuting '${step.name}' test step...\n")

      step.run()
      printStepSeparator()

      step.name
    })

    val cleanupStepProcess = lift[CleanupStep, String](step => {
      printStepSeparator()
      println(s"\nExecuting '${step.name}' cleanup step...\n")

      step.run()
      printStepSeparator()

      step.name
    })

    setupStepProcess(getSetupSteps(suite)).toList

    suite.testCases.foreach(testCase => {
      val executedSteps = testStepProcess(testCase.testSteps).toList
      printStepSeparator()

      println(s"Execution report for test case '${testCase.name}'")
      println(s"\t${executedSteps mkString "\n\t"}")

      printStepSeparator()
    })

    cleanupStepProcess(getCleanupSteps(suite)).toList
  }

  private def getSetupSteps(suite: Suite): Stream[SetupStep] =
    getStepsFrom(suite.setupStep)(_.nextSetupStep)

  private def getCleanupSteps(suite: Suite): Stream[CleanupStep] =
    getStepsFrom(suite.cleanupStep)(_.nextCleanupStep)

}
