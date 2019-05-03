package org.cartagena.tool.core.model

sealed trait Step {

  def name: String

  def profile: Profile

  def context: Context

  def run(): Unit

}

sealed trait SetupStep extends Step {
  def nextSetupStep: SetupStep = NilStep
}

sealed trait TestStep extends Step {
  def nextTestStep: TestStep = NilStep
}

sealed trait CleanupStep extends Step {
  def nextCleanupStep: CleanupStep = NilStep
}

case object NilStep extends SetupStep with TestStep with CleanupStep {

  override val name: String = "Nil step"

  override def profile: Profile =
    throw new UnsupportedOperationException

  override def context: Context =
    throw new UnsupportedOperationException

  override def run(): Unit =
    throw new UnsupportedOperationException

}

abstract class AbstractStep(val profile: Profile, val context: Context) extends Step

abstract class AbstractSetupStep(profile: Profile, context: Context)
  extends AbstractStep(profile, context) with SetupStep

abstract class AbstractCleanupStep(profile: Profile, context: Context)
  extends AbstractStep(profile, context) with CleanupStep

abstract class AbstractTestStep(profile: Profile, context: Context)
  extends AbstractStep(profile, context) with TestStep
