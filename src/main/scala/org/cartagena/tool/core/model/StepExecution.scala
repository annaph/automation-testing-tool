package org.cartagena.tool.core.model

sealed trait StepExecution {

  def stepName: String

  def innerStepExecutions: List[StepExecution]

  def toStepReport: StepReport =
    StepExecution.toStepReport(this)

  def toInnerStepReports: Stream[StepReport] =
    StepExecution.toInnerStepReports(this)

}

case class PassedStepExecution(stepName: String,
                               innerStepExecutions: List[StepExecution] = List.empty) extends StepExecution

sealed trait NonPassedStepExecution extends StepExecution {

  def failure: Option[Throwable]

}

case class FailedStepExecution(stepName: String,
                               error: Throwable,
                               innerStepExecutions: List[StepExecution] = List.empty) extends NonPassedStepExecution {

  override val failure: Option[Throwable] =
    Some(error)

}

case class IgnoredStepExecution(stepName: String,
                                innerStepExecutions: List[StepExecution] = List.empty) extends NonPassedStepExecution {

  override val failure: Option[Throwable] =
    None

}

object StepExecution {

  private def toStepReport(stepExecution: StepExecution): StepReport = ???

  private def toInnerStepReports(stepExecution: StepExecution): Stream[StepReport] = ???

}
