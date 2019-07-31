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
                       status: Status) {

  def toPrettyString: String =
    SuiteReport.toPrettyString(this)

}

object SuiteReport {

  import org.cartagena.tool.core.PrettyPrintConstants._

  private def toPrettyString(suiteReport: SuiteReport): String = {
    val builder = StringBuilder.newBuilder

    def printStepReport(prefixTabs: String = s"$TAB $TAB", middleTabs: String = s"$TAB $TAB"): StepReport => Unit =
      stepReport => {
        builder ++= s"$prefixTabs ==> Name:   $middleTabs ${stepReport.stepName}"
        builder ++= NEW_LINE
        builder ++= s"$prefixTabs     Status: $middleTabs ${stepReport.status}"
        builder ++= NEW_LINE
      }

    builder ++= LINE_SEPARATOR
    builder ++= LINE_SEPARATOR
    builder ++= LINE_SEPARATOR

    builder ++= "SUITE REPORT"
    builder ++= NEW_LINE

    builder ++= s"$TAB ==> Name:   $TAB $TAB $TAB ${suiteReport.suiteName}"
    builder ++= NEW_LINE

    builder ++= s"$TAB ==> Setup steps:"
    builder ++= NEW_LINE

    suiteReport.setupStepReports.foreach(printStepReport())

    builder ++= s"$TAB ==> Test cases:"
    builder ++= NEW_LINE

    suiteReport.testCaseReports.foreach { testCaseReport =>
      builder ++= s"$TAB $TAB ==> Name: $TAB $TAB ${testCaseReport.testCaseName}"
      builder ++= NEW_LINE

      builder ++= s"$TAB $TAB     Steps:"
      builder ++= NEW_LINE

      testCaseReport.stepReports.foreach(printStepReport(s"$TAB $TAB $TAB", s"$TAB"))

      builder ++= s"$TAB $TAB     Status: $TAB $TAB ${testCaseReport.status}"
      builder ++= NEW_LINE
    }

    builder ++= s"$TAB ==> Cleanup steps:"
    builder ++= NEW_LINE

    suiteReport.cleanupStepReports.foreach(printStepReport())

    builder ++= s"$TAB ==> Status: $TAB $TAB $TAB ${suiteReport.status}"
    builder ++= NEW_LINE

    builder ++= LINE_SEPARATOR
    builder ++= LINE_SEPARATOR
    builder ++= LINE_SEPARATOR

    builder.toString()
  }

}
