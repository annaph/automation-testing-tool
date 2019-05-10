package org.cartagena.tool.core.model

sealed trait StepExecutionStatus

case object Passed extends StepExecutionStatus

case object Failed extends StepExecutionStatus

case object Ignored extends StepExecutionStatus

sealed trait StepExecution {

  def stepName: String

  def status: StepExecutionStatus

  def failure: Option[Throwable]

  def innerStepExecutions: List[StepExecution]

}

case class PassedStepExecution(stepName: String,
                               innerStepExecutions: List[StepExecution] = List.empty)
  extends StepExecution {

  override val status: StepExecutionStatus = Passed

  override val failure: Option[Throwable] = None

}

case class FailedStepExecution(stepName: String,
                               error: Throwable,
                               innerStepExecutions: List[StepExecution] = List.empty)
  extends StepExecution {

  override val status: StepExecutionStatus = Failed

  override val failure: Option[Throwable] = Some(error)

}

case class IgnoredStepExecution(stepName: String,
                                innerStepExecutions: List[StepExecution] = List.empty)
  extends StepExecution {

  override val status: StepExecutionStatus = Ignored

  override val failure: Option[Throwable] = None

}
