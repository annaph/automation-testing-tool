package org.cartagena.tool.core.model

sealed trait StepExecution {

  def stepName: String

  def children: List[StepExecution]

  def leafs: List[StepExecution] =
    StepExecution.leafs(this)

  def toStepReport: StepReport =
    StepExecution.toStepReport(this)

}

case class PassedStepExecution(stepName: String,
                               children: List[StepExecution] = List.empty) extends StepExecution

sealed trait NonPassedStepExecution extends StepExecution {

  def failure: Option[Throwable]

}

case class FailedStepExecution(stepName: String,
                               error: Throwable,
                               children: List[StepExecution] = List.empty) extends NonPassedStepExecution {

  override val failure: Option[Throwable] =
    Some(error)

}

case class IgnoredStepExecution(stepName: String,
                                children: List[StepExecution] = List.empty) extends NonPassedStepExecution {

  override val failure: Option[Throwable] =
    None

}

object StepExecution {

  private def leafs(stepExecution: StepExecution): List[StepExecution] =
    stepExecution.children match {
      case head :: tail =>
        leafs(head) ::: tail.flatMap(leafs)
      case Nil =>
        stepExecution :: Nil
    }

  private def toStepReport(stepExecution: StepExecution): StepReport =
    stepExecution match {
      case PassedStepExecution(stepName, _) =>
        StepReport(stepName, Passed)
      case FailedStepExecution(stepName, _, _) =>
        StepReport(stepName, Failed)
      case IgnoredStepExecution(stepName, _) =>
        StepReport(stepName, Ignored)
    }

}
