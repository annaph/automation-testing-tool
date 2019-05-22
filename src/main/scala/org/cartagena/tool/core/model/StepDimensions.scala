package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepExtensions.InfoMessages
import org.cartagena.tool.core.model.StepX.{executeRouterStep, executeSerialStep, executeShapelessStep, ignoreStep}

object StepDimensions {

  trait StepType {
    self: StepX =>
  }

  trait Setup extends StepType {
    self: StepX =>
  }

  trait Test extends StepType {
    self: StepX =>
  }

  trait Cleanup extends StepType {
    self: StepX =>
  }

  trait StepShape {
    self: StepX =>

    private[model] def execute(): StepExecution

    private[model] def ignore: StepExecution =
      ignoreStep(this)

    private[model] def toStream: Stream[StepX with StepShape] =
      this #:: Stream.empty[StepX with StepShape]

  }

  trait Shapeless extends StepShape {
    self: StepX with InfoMessages =>

    override private[model] def execute(): StepExecution =
      executeShapelessStep(this)

  }

  trait Router extends StepShape {
    self: StepX =>

    def route(): StepX with StepShape

    override private[model] def execute(): StepExecution =
      executeRouterStep(this)

  }

  trait Serial extends StepShape {
    self: StepX =>

    def left: StepX with StepShape

    def right: () => StepX with StepShape

    override private[model] def execute(): StepExecution =
      executeSerialStep(this)

    override private[model] def toStream: Stream[StepX with StepShape] =
      left.toStream ++: right.apply().toStream

  }

}
