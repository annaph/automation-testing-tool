package org.cartagena.tool.core

trait TestCase {

  def name: String

  def profile: Profile

  def firstStep: TestStep

  def testSteps: Stream[TestStep] =
    TestCase getTestSteps this

}

object TestCase {

  private def getTestSteps(testCase: TestCase): Stream[TestStep] =
    getStepsFrom(testCase.firstStep)

}
