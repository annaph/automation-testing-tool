package org.cartagena.tool.core.model

trait TestCase {

  def name: String

  def firstStep: TestStep = NilStep

  def testSteps: Stream[TestStep] =
    TestCase getTestSteps this

}

object TestCase {

  private def getTestSteps(testCase: TestCase): Stream[TestStep] =
    getStepsFrom(testCase.firstStep)(_.nextTestStep)

}
