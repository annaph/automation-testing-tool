package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepDimensions.{Cleanup, Setup, StepShape, Test}

trait ShapedSetupStep extends Step with Setup with StepShape {

  def +(other: => ShapedSetupStep): SerialSetupStep =
    add(other)

  def add(other: => ShapedSetupStep): SerialSetupStep =
    Step.add(this, other)

}

trait ShapedTestStep extends Step with Test with StepShape {

  def +(other: => ShapedTestStep): SerialTestStep =
    add(other)

  def add(other: => ShapedTestStep): SerialTestStep =
    Step.add(this, other)

}

trait ShapedCleanupStep extends Step with Cleanup with StepShape {

  def +(other: => ShapedCleanupStep): SerialCleanupStep =
    add(other)

  def add(other: => ShapedCleanupStep): SerialCleanupStep =
    Step.add(this, other)

}
