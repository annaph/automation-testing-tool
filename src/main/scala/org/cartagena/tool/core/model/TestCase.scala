package org.cartagena.tool.core.model

trait TestCase {

  def name: String

  def testSteps: SerialTestStep

  def run(): TestCaseReport =
    TestCase.run(this)

}

object TestCase {

  private def run(testCase: TestCase): TestCaseReport = {
    val testStepsExecution = testCase.testSteps.execute()

    println(s"Execution report for test case '${testCase.name}'")
    println(s"\t${testStepsExecution.children mkString "\n\t"}")

    TestCaseReport(
      testCase.name,
      testStepsExecution.leafs.map(_.toStepReport),
      testStepsExecution.toStepReport.status)
  }

}
