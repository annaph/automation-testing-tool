package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Step.{executeEndStep, ignoreStep}
import org.cartagena.tool.core.model.StepDimensions._
import org.cartagena.tool.core.model.StepExtensions.{InfoMessages, ProfileAndContext, UnsupportedRunnable}

case object EmptyStep extends Step
  with Setup
  with Test
  with Cleanup
  with Shapeless
  with Router
  with Serial
  with InfoMessages
  with ProfileAndContext
  with UnsupportedRunnable {

  override val name: String = "Empty step"

  override def route(): Step with StepShape =
    throw new UnsupportedOperationException

  override def left: Step with StepShape =
    throw new UnsupportedOperationException

  override def right: () => Step with StepShape =
    throw new UnsupportedOperationException

  override private[model] def execute(): StepExecution =
    executeEndStep(this)

  override private[model] def ignore: StepExecution =
    ignoreStep(this)

  override private[model] def toStream: Stream[Step with StepShape] =
    Stream.empty

}
