package org.cartagena.tool.core.model

sealed trait Status

case object Passed extends Status

case object Failed extends Status

case object Ignored extends Status

case class StepReport(stepName: String,
                      status: Status)

case class TestCaseReport(testCaseName: String,
                          stepReports: List[StepReport],
                          status: Status)

case class SuiteReport(suiteName: String,
                       setupStepReports: List[StepReport],
                       testCaseReports: List[TestCaseReport],
                       cleanupStepReports: List[StepReport],
                       status: Status)
