package org.cartagena.tool.core.model

import org.cartagena.tool.core.CartagenaUtils._
import org.cartagena.tool.core.model.StepTestStructures.{MyCleanupStep => CleanupStep, MyCleanupStepToFail => CleanupStepToFail, MySetupStep => SetupStep, MySetupStepToFail => SetupStepToFail, MyShapelessStep1 => S1, MyShapelessStep2 => S2, MyShapelessStep2ToFail => S2ToFail, MyShapelessStep3 => S3, MyShapelessStep3ToFail => S3ToFail, MyShapelessStepToFail1 => S1ToFail}
import org.cartagena.tool.core.model.SuiteTest._
import org.scalatest.{FlatSpec, Matchers}

class SuiteTest extends FlatSpec with Matchers {

  "run" should "run suite and return suite report with Passed status" in {
    // given
    val suite = MySuite()

    val expectedSetupStepReports = StepReport(SetupStep.name, Passed) :: Nil

    val expectedTestCaseReports =
      List(
        TestCaseReport(
          TEST_CASE_NAME_1,
          StepReport(S1.name, Passed) :: Nil,
          Passed),
        TestCaseReport(
          TEST_CASE_NAME_2,
          StepReport(S2.name, Passed) :: Nil,
          Passed),
        TestCaseReport(
          TEST_CASE_NAME_3,
          StepReport(S3.name, Passed) :: Nil,
          Passed))

    val expectedCleanupStepReports = StepReport(CleanupStep.name, Passed) :: Nil

    // when
    val actual = suite.run()

    // then
    actual should be(
      SuiteReport(
        SUITE_NAME,
        expectedSetupStepReports,
        expectedTestCaseReports,
        expectedCleanupStepReports,
        Passed))
  }

  it should "run suite, fail on setup step and return suite report with Failed status" in {
    // given
    val suite: Suite = new MySuite {

      override val setupSteps: SerialSetupStep =
        SetupStepToFail

    }

    val expectedSetupStepReports = StepReport(SetupStepToFail.name, Failed) :: Nil

    val expectedTestCaseReports =
      List(
        TestCaseReport(
          TEST_CASE_NAME_1,
          List.empty,
          Ignored),
        TestCaseReport(
          TEST_CASE_NAME_2,
          List.empty,
          Ignored),
        TestCaseReport(
          TEST_CASE_NAME_3,
          List.empty,
          Ignored))

    val expectedCleanupStepReports = StepReport(suite.cleanupSteps.name, Ignored) :: Nil

    // when
    val actual = suite.run()

    // then
    actual should be(
      SuiteReport(
        SUITE_NAME,
        expectedSetupStepReports,
        expectedTestCaseReports,
        expectedCleanupStepReports,
        Failed))
  }

  it should "run suite, fail on 1st test case and return suite report with Failed status" in {
    // given
    val suite: Suite = new MySuite {

      override def testCases: List[TestCase] =
        List(
          createTestCase(TEST_CASE_NAME_1, S1ToFail),
          createTestCase(TEST_CASE_NAME_2, S2),
          createTestCase(TEST_CASE_NAME_3, S3))

    }

    val expectedSetupStepReports = StepReport(SetupStep.name, Passed) :: Nil

    val expectedTestCaseReports =
      List(
        TestCaseReport(
          TEST_CASE_NAME_1,
          StepReport(S1ToFail.name, Failed) :: Nil,
          Failed),
        TestCaseReport(
          TEST_CASE_NAME_2,
          List.empty,
          Ignored),
        TestCaseReport(
          TEST_CASE_NAME_3,
          List.empty,
          Ignored))

    val expectedCleanupStepReports = StepReport(suite.cleanupSteps.name, Ignored) :: Nil

    // when
    val actual = suite.run()

    // then
    actual should be(
      SuiteReport(
        SUITE_NAME,
        expectedSetupStepReports,
        expectedTestCaseReports,
        expectedCleanupStepReports,
        Failed))
  }

  it should "run suite, fail on 2nd test case and return suite report with Failed status" in {
    // given
    val suite: Suite = new MySuite {

      override def testCases: List[TestCase] =
        List(
          createTestCase(TEST_CASE_NAME_1, S1),
          createTestCase(TEST_CASE_NAME_2, S2ToFail),
          createTestCase(TEST_CASE_NAME_3, S3))

    }

    val expectedSetupStepReports = StepReport(SetupStep.name, Passed) :: Nil

    val expectedTestCaseReports =
      List(
        TestCaseReport(
          TEST_CASE_NAME_1,
          StepReport(S1.name, Passed) :: Nil,
          Passed),
        TestCaseReport(
          TEST_CASE_NAME_2,
          StepReport(S2ToFail.name, Failed) :: Nil,
          Failed),
        TestCaseReport(
          TEST_CASE_NAME_3,
          List.empty,
          Ignored))

    val expectedCleanupStepReports = StepReport(suite.cleanupSteps.name, Ignored) :: Nil

    // when
    val actual = suite.run()

    // then
    actual should be(
      SuiteReport(
        SUITE_NAME,
        expectedSetupStepReports,
        expectedTestCaseReports,
        expectedCleanupStepReports,
        Failed))
  }

  it should "run suite, fail on last test case and return suite report with Failed status" in {
    // given
    val suite: Suite = new MySuite {

      override def testCases: List[TestCase] =
        List(
          createTestCase(TEST_CASE_NAME_1, S1),
          createTestCase(TEST_CASE_NAME_2, S2),
          createTestCase(TEST_CASE_NAME_3, S3ToFail))

    }

    val expectedSetupStepReports = StepReport(SetupStep.name, Passed) :: Nil

    val expectedTestCaseReports =
      List(
        TestCaseReport(
          TEST_CASE_NAME_1,
          StepReport(S1.name, Passed) :: Nil,
          Passed),
        TestCaseReport(
          TEST_CASE_NAME_2,
          StepReport(S2.name, Passed) :: Nil,
          Passed),
        TestCaseReport(
          TEST_CASE_NAME_3,
          StepReport(S3ToFail.name, Failed) :: Nil,
          Failed))

    val expectedCleanupStepReports = StepReport(suite.cleanupSteps.name, Ignored) :: Nil

    // when
    val actual = suite.run()

    // then
    actual should be(
      SuiteReport(
        SUITE_NAME,
        expectedSetupStepReports,
        expectedTestCaseReports,
        expectedCleanupStepReports,
        Failed))
  }

  it should "run suite, fail on cleanup step and return suite report with Failed status" in {
    // given
    val suite: Suite = new MySuite {

      override def cleanupSteps: SerialCleanupStep =
        CleanupStepToFail

    }

    val expectedSetupStepReports = StepReport(SetupStep.name, Passed) :: Nil

    val expectedTestCaseReports =
      List(
        TestCaseReport(
          TEST_CASE_NAME_1,
          StepReport(S1.name, Passed) :: Nil,
          Passed),
        TestCaseReport(
          TEST_CASE_NAME_2,
          StepReport(S2.name, Passed) :: Nil,
          Passed),
        TestCaseReport(
          TEST_CASE_NAME_3,
          StepReport(S3.name, Passed) :: Nil,
          Passed))

    val expectedCleanupStepReports = StepReport(CleanupStepToFail.name, Failed) :: Nil

    // when
    val actual = suite.run()

    // then
    actual should be(
      SuiteReport(
        SUITE_NAME,
        expectedSetupStepReports,
        expectedTestCaseReports,
        expectedCleanupStepReports,
        Failed))
  }

}

object SuiteTest {

  val SUITE_NAME = "mySuite"

  val TEST_CASE_NAME_1 = "myTestCase1"

  val TEST_CASE_NAME_2 = "myTestCase2"

  val TEST_CASE_NAME_3 = "myTestCase3"

  def createTestCase(testCaseNme: String, testStep: ShapelessTestStep): TestCase =
    new TestCase {

      override val name: String = testCaseNme

      override val testSteps: SerialTestStep =
        testStep
    }

  case class MySuite() extends Suite {

    override val name: String = SUITE_NAME

    override def setupSteps: SerialSetupStep =
      SetupStep

    override def testCases: List[TestCase] =
      List(
        createTestCase(TEST_CASE_NAME_1, S1),
        createTestCase(TEST_CASE_NAME_2, S2),
        createTestCase(TEST_CASE_NAME_3, S3))

    override def cleanupSteps: SerialCleanupStep =
      CleanupStep

  }

}
