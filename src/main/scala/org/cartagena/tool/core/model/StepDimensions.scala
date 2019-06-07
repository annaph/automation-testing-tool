package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Step.{executeRouterStep, executeSerialStep, executeShapelessStep, ignoreStep}
import org.cartagena.tool.core.model.StepExtensions.InfoMessages

object StepDimensions {

  trait StepType {
    self: Step =>
  }

  trait Setup extends StepType {
    self: Step =>
  }

  trait Test extends StepType {
    self: Step =>
  }

  trait Cleanup extends StepType {
    self: Step =>
  }

  trait StepShape {
    self: Step =>

    private[model] def execute(): StepExecution

    private[model] def ignore: StepExecution =
      ignoreStep(this)

    private[model] def toStream: Stream[Step with StepShape] =
      this #:: Stream.empty

  }

  trait Shapeless extends StepShape {
    self: Step with InfoMessages =>

    override private[model] def execute(): StepExecution =
      executeShapelessStep(this)

  }

  trait Router extends StepShape {
    self: Step =>

    def route(): Step with StepShape

    override private[model] def execute(): StepExecution =
      executeRouterStep(this)

  }

  trait Serial extends StepShape {
    self: Step =>

    def left: Step with StepShape

    def right: () => Step with StepShape

    override private[model] def execute(): StepExecution =
      executeSerialStep(this)

    override private[model] def toStream: Stream[Step with StepShape] =
      left.toStream ++: right.apply().toStream

  }

}
