package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepDimensions._
import org.cartagena.tool.core.model.StepExtensions.{InfoMessages, ProfileAndContext, UnsupportedRunnable}
import org.cartagena.tool.core.model.StepX.{executeEndStep, ignoreStep}

case object EmptyStep extends StepX
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

  override def route(): StepX with StepShape =
    throw new UnsupportedOperationException

  override def left: StepX with StepShape =
    throw new UnsupportedOperationException

  override def right: () => StepX with StepShape =
    throw new UnsupportedOperationException

  override private[model] def execute(): StepExecution =
    executeEndStep(this)

  override private[model] def ignore: StepExecution =
    ignoreStep(this)

  override private[model] def toStream: Stream[StepX with StepShape] =
    Stream.empty

}
