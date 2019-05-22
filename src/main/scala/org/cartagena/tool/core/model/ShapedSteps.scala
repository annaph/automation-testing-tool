package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepDimensions.{Cleanup, Setup, StepShape, Test}

trait ShapedSetupStep extends StepX with Setup with StepShape {

  def +(other: => ShapedSetupStep): SerialSetupStep =
    add(other)

  def add(other: => ShapedSetupStep): SerialSetupStep =
    StepX.add(this, other)

}

trait ShapedTestStep extends StepX with Test with StepShape {

  def +(other: => ShapedTestStep): SerialTestStep =
    add(other)

  def add(other: => ShapedTestStep): SerialTestStep =
    StepX.add(this, other)

}

trait ShapedCleanupStep extends StepX with Cleanup with StepShape {

  def +(other: => ShapedCleanupStep): SerialCleanupStep =
    add(other)

  def add(other: => ShapedCleanupStep): SerialCleanupStep =
    StepX.add(this, other)

}
