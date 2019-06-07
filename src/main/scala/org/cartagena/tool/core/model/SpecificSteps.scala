package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepDimensions.{Router, Serial, Shapeless, StepShape}
import org.cartagena.tool.core.model.StepExtensions.{InfoMessages, ProfileAndContext, UnsupportedRunnable}

trait ShapelessSetupStep extends ShapedSetupStep
  with Shapeless
  with InfoMessages
  with ProfileAndContext

trait ShapelessTestStep extends ShapedTestStep
  with Shapeless
  with InfoMessages
  with ProfileAndContext

trait ShapelessCleanupStep extends ShapedCleanupStep
  with Shapeless
  with InfoMessages
  with ProfileAndContext

trait RouterSetupStep extends ShapedSetupStep
  with Router
  with ProfileAndContext

trait RouterTestStep extends ShapedTestStep
  with Router
  with ProfileAndContext

trait RouterCleanupStep extends ShapedCleanupStep
  with Router
  with ProfileAndContext

final case class SerialSetupStep(left: Step with StepShape, right: () => Step with StepShape)
  extends ShapedSetupStep
    with Serial
    with ProfileAndContext
    with UnsupportedRunnable {

  override def name: String = "Serial setup step"

}

final case class SerialTestStep(left: Step with StepShape, right: () => Step with StepShape)
  extends ShapedTestStep
    with Serial
    with ProfileAndContext
    with UnsupportedRunnable {

  override def name: String = "Serial test step"

}

final case class SerialCleanupStep(left: Step with StepShape, right: () => Step with StepShape)
  extends ShapedCleanupStep
    with Serial
    with ProfileAndContext
    with UnsupportedRunnable {

  override def name: String = "Serial cleanup step"

}

object SerialSetupStep {

  private val _emptySerialSetupStep = SerialSetupStep(EmptyStep, () => EmptyStep)

  def empty: SerialSetupStep =
    _emptySerialSetupStep

}

object SerialTestStep {

  private val _emptySerialTestStep = SerialTestStep(EmptyStep, () => EmptyStep)

  def empty: SerialTestStep =
    _emptySerialTestStep

}

object SerialCleanupStep {

  private val _emptySerialCleanupStep = SerialCleanupStep(EmptyStep, () => EmptyStep)

  def empty: SerialCleanupStep =
    _emptySerialCleanupStep

}

case object RouterStepFailed extends Exception("Router step execution failed!")

case object SerialStepFailed extends Exception("Serial step execution failed!")
