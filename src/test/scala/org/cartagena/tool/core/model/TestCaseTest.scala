package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepTestStructures.{MyShapelessStep => S1, MyShapelessStep2 => S2, MyShapelessStep2ToFail => S2ToFail, MyShapelessStep3 => S3, MyShapelessStep3ToFail => S3ToFail, MyShapelessStepToFail => S1ToFail}
import org.cartagena.tool.core.model.TestCaseTest.TEST_CASE_NAME
import org.scalatest.{FlatSpec, Matchers}

class TestCaseTest extends FlatSpec with Matchers {

  "run" should "run test case 'S1 + S2 + S3' and return test case report with Passed status" in {
    // given
    val testCase: TestCase = new TestCase {

      override val name: String = TEST_CASE_NAME

      override val testSteps: SerialTestStep = S1 + S2 + S3

    }

    val expectedStepReports = List(
      StepReport(S1.name, Passed),
      StepReport(S2.name, Passed),
      StepReport(S3.name, Passed))

    // when
    val actual = testCase.run()

    // then
    actual should be(TestCaseReport(TEST_CASE_NAME, expectedStepReports, Passed))
  }

  it should "run test case 'S1ToFail + S2 + S3' and return test case report with Failed status" in {
    // given
    val testCase: TestCase = new TestCase {

      override val name: String = TEST_CASE_NAME

      override val testSteps: SerialTestStep = S1ToFail + S2 + S3

    }

    val expectedStepReports = List(
      StepReport(S1ToFail.name, Failed),
      StepReport(S2.name, Ignored),
      StepReport(S3.name, Ignored))

    // when
    val actual = testCase.run()

    // then
    actual should be(TestCaseReport(TEST_CASE_NAME, expectedStepReports, Failed))
  }

  it should "run test case 'S1 + S2ToFail + S3' and return test case report with Failed status" in {
    // given
    val testCase: TestCase = new TestCase {

      override val name: String = TEST_CASE_NAME

      override val testSteps: SerialTestStep =
        S1 + S2ToFail + S3

    }

    val expectedStepReports = List(
      StepReport(S1.name, Passed),
      StepReport(S2ToFail.name, Failed),
      StepReport(S3.name, Ignored))

    // when
    val actual = testCase.run()

    // then
    actual should be(TestCaseReport(TEST_CASE_NAME, expectedStepReports, Failed))
  }

  it should "run test case 'S1 + S2 + S3ToFail' and return test case report with Failed status" in {
    // given
    val testCase: TestCase = new TestCase {

      override val name: String = TEST_CASE_NAME

      override val testSteps: SerialTestStep =
        S1 + S2 + S3ToFail

    }

    val expectedStepReports = List(
      StepReport(S1.name, Passed),
      StepReport(S2.name, Passed),
      StepReport(S3ToFail.name, Failed))

    // when
    val actual = testCase.run()

    // then
    actual should be(TestCaseReport(TEST_CASE_NAME, expectedStepReports, Failed))
  }

}

object TestCaseTest {

  val TEST_CASE_NAME = "myTestCase"

}
