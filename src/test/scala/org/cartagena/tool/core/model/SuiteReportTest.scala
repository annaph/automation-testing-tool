package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepTestUtil.{MyCleanupStep, MySetupStep, MyShapelessStep1 => MyStep}
import org.scalatest.{FlatSpec, Matchers}

class SuiteReportTest extends FlatSpec with Matchers {

  "toPrettyString" should "prettify suite report" in {
    // given
    val suiteReport = SuiteReport(
      suiteName = "MySuite",
      setupStepReports = StepReport(MySetupStep.name, Passed) :: Nil,
      testCaseReports = List(
        TestCaseReport("MyTestCase", StepReport(MyStep.name, Passed) :: Nil, Passed)),
      cleanupStepReports = StepReport(MyCleanupStep.name, Passed) :: Nil,
      status = Passed)

    val expected = "" +
      "-------------------------------------------------------------------------------------------------\n" +
      "-------------------------------------------------------------------------------------------------\n" +
      "-------------------------------------------------------------------------------------------------\n" +
      "SUITE REPORT\n" +
      "\t ==> Name:   \t \t \t MySuite" +
      s"\n\t ==> Setup steps:\n\t \t ==> Name:   \t \t ${MySetupStep.name}" +
      "\n\t \t     Status: \t \t Passed\n" +
      "\t ==> Test cases:\n" +
      "\t \t ==> Name: \t \t MyTestCase\n" +
      "\t \t     Steps:\n" +
      s"\t \t \t ==> Name:   \t ${MyStep.name}\n" +
      "\t \t \t     Status: \t Passed\n" +
      "\t \t     Status: \t \t Passed\n" +
      "\t ==> Cleanup steps:\n" +
      "\t \t ==> Name:   \t \t My Cleanup Step\n" +
      "\t \t     Status: \t \t Passed\n" +
      "\t ==> Status: \t \t \t Passed\n" +
      "-------------------------------------------------------------------------------------------------\n" +
      "-------------------------------------------------------------------------------------------------\n" +
      "-------------------------------------------------------------------------------------------------\n"

    // when
    val actual = suiteReport.toPrettyString

    // then
    actual should be(expected)
  }

}
