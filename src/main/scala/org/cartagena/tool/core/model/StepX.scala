package org.cartagena.tool.core.model

import scala.util.{Failure, Success, Try}

/**
  * Type dimension
  */
sealed trait StepType {
  self: StepX =>
}

sealed trait Setup extends StepType {
  self: StepX =>
}

sealed trait Test extends StepType {
  self: StepX =>
}

sealed trait Cleanup extends StepType {
  self: StepX =>
}

/**
  * Shape dimension
  */
sealed trait StepShape {
  self: StepX =>

  private[model] def execute(): StepExecution

  private[model] def ignore: StepExecution =
    StepX.ignoreStep(this)

  private[model] def toStream: Stream[StepX with StepShape] =
    this #:: Stream.empty[StepX with StepShape]

}

sealed trait Shapeless extends StepShape {
  self: StepX with StepExecutionMsg =>

  override private[model] def execute(): StepExecution =
    StepX.executeShapelessStep(this)

}

sealed trait Router extends StepShape {
  self: StepX =>

  def route(): StepShape

  override private[model] def execute(): StepExecution =
    StepX.executeRouterStep(this)

}

sealed trait Serial extends StepShape {
  self: StepX =>

  def left: StepX with StepShape

  def right: () => StepX with StepShape

  override private[model] def execute(): StepExecution =
    StepX.executeSerialStep(this)

  override private[model] def toStream: Stream[StepX with StepShape] =
    left.toStream ++: right.apply().toStream

}

/**
  * Step messages
  */
sealed trait StepExecutionMsg {
  self: StepX =>

  def preRunMsg: String =
    s"Executing '$name'..."

  def postRunPassedMsg: String =
    s"Finish executing '$name' with success."

  def postRunFailedMsg: String =
    s"Finish executing '$name' with failure!"

  def postRunIgnoredMsg: String =
    s"Execution of '$name' ignored!"

}

/**
  * Step
  */
sealed trait StepX {

  def name: String

  def profile: Option[Profile]

  def context: Option[Context]

  def run(): Unit

}

/**
  * End Step
  */
case object EndStep
  extends StepX with Setup with Test with Cleanup with Shapeless with Router with Serial with StepExecutionMsg {

  override val name: String =
    "End step"

  override val profile: Option[Profile] =
    None

  override val context: Option[Context] =
    None

  override def run(): Unit =
    throw new UnsupportedOperationException

  override def route(): StepShape =
    throw new UnsupportedOperationException

  override def left: StepX with StepShape =
    throw new UnsupportedOperationException

  override def right: () => StepX with StepShape =
    throw new UnsupportedOperationException

  override private[model] def execute(): StepExecution =
    StepX.executeEndStep(this)

}

/**
  * Steps with type dimension
  */
sealed trait SetupStepX extends StepX with Setup

sealed trait TestStepX extends StepX with Test

sealed trait CleanupStepX extends StepX with Cleanup

/**
  * Steps with shape dimension
  */
sealed trait ShapelessStepX extends StepX with Shapeless with StepExecutionMsg {

  val stepProfile: Profile

  val stepContext: Context

  override def profile: Option[Profile] = Some(stepProfile)

  override def context: Option[Context] = Some(stepContext)

}

sealed trait RouterStepX extends StepX with Router {

  val stepProfile: Profile

  val stepContext: Context

  def alternativeSteps: List[StepX]

  override def profile: Option[Profile] = Some(stepProfile)

  override def context: Option[Context] = Some(stepContext)

}

sealed trait SerialStepX extends StepX with Serial {

  override val profile: Option[Profile] = None

  override val context: Option[Context] = None

  // TODO: unsupported exception
  override def run(): Unit = {}

}

/**
  * Steps with specific type dimension and any shape dimension
  */
sealed trait ShapedSetupStep extends SetupStepX with StepShape {

  def +(other: => ShapedSetupStep): SerialSetupStepX =
    add(other)

  def add(other: => ShapedSetupStep): SerialSetupStepX =
    StepX.add(this, other)

}

sealed trait ShapedTestStep extends TestStepX with StepShape {

  def +(other: => ShapedTestStep): SerialTestStepX =
    add(other)

  def add(other: => ShapedTestStep): SerialTestStepX =
    StepX.add(this, other)

}

sealed trait ShapedCleanupStep extends CleanupStepX with StepShape {

  def +(other: => ShapedCleanupStep): SerialCleanupStepX =
    add(other)

  def add(other: => ShapedCleanupStep): SerialCleanupStepX =
    StepX.add(this, other)

}

/**
  * Steps with specific type and specifc shapeless dimension
  */
abstract class ShapelessSetupStepX(stepProfile: Profile, stepContext: Context)
  extends ShapedSetupStep with ShapelessStepX

abstract class ShapelessTestStepX(stepProfile: Profile, stepContext: Context)
  extends ShapedTestStep with ShapelessStepX

abstract class ShapelessCleanupStepX(stepProfile: Profile, stepContext: Context)
  extends ShapedCleanupStep with ShapelessStepX

/**
  * Steps with type and router dimension
  */
abstract class RouterSetupStep(stepProfile: Profile, stepContext: Context, alternativeSteps: List[StepX])
  extends ShapedSetupStep with RouterStepX

abstract class RouterTestStep(stepProfile: Profile, stepContext: Context, alternativeSteps: List[StepX])
  extends ShapedTestStep with RouterStepX

abstract class RouterCleanupStep(stepProfile: Profile, stepContext: Context, alternativeSteps: List[StepX])
  extends ShapedCleanupStep with RouterStepX

/**
  * Steps with type and serial dimension
  */
final case class SerialSetupStepX(left: StepX with StepShape, right: () => StepX with StepShape)
  extends ShapedSetupStep with SerialStepX {

  override def name: String = "Serial setup step"

}

final case class SerialTestStepX(left: StepX with StepShape, right: () => StepX with StepShape)
  extends ShapedTestStep with SerialStepX {

  override def name: String = "Serial test step"

}

final case class SerialCleanupStepX(left: StepX with StepShape, right: () => StepX with StepShape)
  extends ShapedCleanupStep with SerialStepX {

  override def name: String = "Serial cleanup step"

}

case object RouterStepFailed extends Exception("Router step execution failed!")

case object SerialStepFailed extends Exception("Serial step execution failed!")

object StepX {

  def executeEndStep(step: EndStep.type): StepExecution = {
    println(step.preRunMsg)
    println(step.postRunPassedMsg)

    PassedStepExecution(step.name)
  }

  def executeShapelessStep[T <: StepX with Shapeless with StepExecutionMsg](step: T): StepExecution = {
    // TODO: unit tests
    println(step.preRunMsg)

    Try {
      step.run()
    } match {
      case Success(_) =>
        println(step.postRunPassedMsg)
        PassedStepExecution(step.name)
      case Failure(ex) =>
        println(step.postRunFailedMsg)
        FailedStepExecution(step.name, ex)
    }
  }

  // TODO: unit tests
  def executeRouterStep[T <: StepX with Router](step: T): StepExecution =
    step.route().execute() match {
      case x: PassedStepExecution =>
        PassedStepExecution(step.name, List(x))
      case x: StepExecution =>
        FailedStepExecution(step.name, RouterStepFailed, List(x))
    }

  // TODO: unit tests
  def executeSerialStep[T <: StepX with Serial](step: T): StepExecution = ???

  def ignoreStep(step: StepX): StepExecution =
    IgnoredStepExecution(step.name)

  def add(left: ShapedSetupStep, right: => ShapedSetupStep): SerialSetupStepX = {
    lazy val r = right
    SerialSetupStepX(left, () => r)
  }

  def add(left: ShapedTestStep, right: => ShapedTestStep): SerialTestStepX = {
    lazy val r = right
    SerialTestStepX(left, () => r)
  }

  def add(left: ShapedCleanupStep, right: => ShapedCleanupStep): SerialCleanupStepX = {
    lazy val r = right
    SerialCleanupStepX(left, () => r)
  }

}
