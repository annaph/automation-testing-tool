package org.cartagena.tool.core.model

trait TestCase {

  def name: String

  def run(): StepExecution =
    testSteps.execute()

  def testSteps: SerialTestStep =
    SerialTestStep.empty

}
