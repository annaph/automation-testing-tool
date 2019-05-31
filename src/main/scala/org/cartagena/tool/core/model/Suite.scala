package org.cartagena.tool.core.model

trait Suite {

  def name: String

  def setupSteps: SerialSetupStep =
    SerialSetupStep.empty

  def testCases: List[TestCase]

  def cleanupSteps: SerialCleanupStep =
    SerialCleanupStep.empty

  def run(): Unit =
    Suite run this

}

object Suite {

  private def run(suite: Suite): Unit = {

    suite.setupSteps.execute()

    suite.testCases.foreach { testCase =>
      val stepExecution = testCase.testSteps.execute()

      println(s"Execution report for test case '${testCase.name}'")
      println(s"\t${stepExecution.innerStepExecutions mkString "\n\t"}")
    }

    suite.cleanupSteps.execute()

  }

}
