package org.cartagena.tool.core.model

sealed trait Context[T] {

  def get: T

  def update(obj: T): T

}

case class TestContext[T](value: T) extends Context[T] {
  private var _value: T = value

  override def get: T = _value

  override def update(obj: T): T = {
    _value = obj
    _value
  }

}

case object EmptyContext extends Context[Nothing] {
  override def get: Nothing =
    throw new UnsupportedOperationException

  override def update(obj: Nothing): Nothing =
    throw new UnsupportedOperationException

}

sealed trait Step {
  type C

  def name: String

  def profile: Profile

  def context: Context[C]

  def execute(): Unit

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
  override type C = Nothing

  override val name: String = "Nill step"

  override def profile: Profile =
    throw new UnsupportedOperationException

  override def context: Context[C] =
    throw new UnsupportedOperationException

  override def execute(): Unit =
    throw new UnsupportedOperationException

}

abstract class AbstractStep[T](val profile: Profile, val context: Context[T]) extends Step {
  override type C = T
}

abstract class AbstractSetupStep[T](profile: Profile, context: Context[T])
  extends AbstractStep[T](profile, context) with SetupStep


abstract class AbstractCleanupStep[T](profile: Profile, context: Context[T])
  extends AbstractStep[T](profile, context) with CleanupStep

abstract class AbstractTestStep[T](profile: Profile, context: Context[T])
  extends AbstractStep[T](profile, context) with TestStep
