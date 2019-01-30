package org.cartagena.tool.core

trait Context[T] {

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

trait Step {
  type C

  val name: String

  def profile: Profile

  def context: Context[C]

  def execute(): Unit

  def nextStep: Option[Step]
}

trait SetupStep extends Step {
  override def nextStep: Option[SetupStep]
}

trait CleanupStep extends Step {
  override def nextStep: Option[CleanupStep]
}

trait TestStep extends Step {
  override def nextStep: Option[TestStep]
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
