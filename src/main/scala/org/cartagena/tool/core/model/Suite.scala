package org.cartagena.tool.core.model

trait Suite {

  def name: String

  def setupSteps: SerialSetupStep

  def testCases: List[TestCase]

  def cleanupSteps: SerialCleanupStep

  def run(): SuiteReport =
    Suite run this

}

object Suite {

  import scala.collection.mutable.ListBuffer

  private def run(suite: Suite): SuiteReport = {

    val (setupStatus, setupStepReports) = executeSerialSetupStep(suite.setupSteps)

    val (testCasesStatus: Status, testCaseReports) = suite.testCases
      .foldLeft(setupStatus -> ListBuffer.empty[TestCaseReport]) {
        case ((prevStatus, reports), testCase) =>
          val (newStatus, testCaseReport) = executeTestCase(prevStatus, testCase)

          newStatus -> (reports :+ testCaseReport)
      }

    val (status, cleanupStepReports) = executeSerialCleanupStep(testCasesStatus, suite.cleanupSteps)

    SuiteReport(
      suite.name,
      setupStepReports,
      testCaseReports.toList,
      cleanupStepReports,
      status)
  }

  private def executeSerialSetupStep(step: SerialSetupStep): (Status, List[StepReport]) =
    toStatusAndStepReports(step.execute())

  private def executeTestCase(status: Status, testCase: TestCase): (Status, TestCaseReport) =
    status match {
      case Passed =>
        executeTestCase(testCase)
      case _ =>
        Failed -> TestCaseReport(testCase.name, List.empty, Ignored)
    }

  private def executeSerialCleanupStep(status: Status, step: SerialCleanupStep): (Status, List[StepReport]) =
    status match {
      case Passed =>
        toStatusAndStepReports(step.execute())
      case _ =>
        Failed -> (StepReport(step.name, Ignored) :: Nil)
    }

  private def executeTestCase(testCase: TestCase): (Status, TestCaseReport) =
    testCase.run() match {
      case x@TestCaseReport(_, _, Passed) =>
        Passed -> x
      case x@TestCaseReport(_, _, _) =>
        Failed -> x
    }

  private def toStatusAndStepReports(stepExecution: StepExecution): (Status, List[StepReport]) = {
    val status = stepExecution match {
      case _: PassedStepExecution =>
        Passed
      case _: NonPassedStepExecution =>
        Failed
    }

    status -> stepExecution.leafs.map(_.toStepReport)
  }

}
