package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process.{emit, lift}
import org.cartagena.tool.core.model.StepDimensions.{Router, Serial, Shapeless, StepShape}
import org.cartagena.tool.core.model.StepExtensions.InfoMessages

import scala.util.{Failure, Success, Try}

trait StepX {

  def name: String

  def profile: Profile

  def context: Context

  def run(): Unit

}

object StepX {

  private[model] def add(left: ShapedSetupStep, right: => ShapedSetupStep): SerialSetupStep = {
    lazy val r = right
    SerialSetupStep(left, () => r)
  }

  private[model] def add(left: ShapedTestStep, right: => ShapedTestStep): SerialTestStep = {
    lazy val r = right
    SerialTestStep(left, () => r)
  }

  private[model] def add(left: ShapedCleanupStep, right: => ShapedCleanupStep): SerialCleanupStep = {
    lazy val r = right
    SerialCleanupStep(left, () => r)
  }

  private[model] def executeEndStep(step: EmptyStep.type): StepExecution = {
    println(step.preRunMsg)
    println(step.passedRunMsg)

    PassedStepExecution(step.name)
  }

  private[model] def executeShapelessStep[T <: StepX with Shapeless with InfoMessages](step: T): StepExecution = {
    println(step.preRunMsg)

    Try {
      step.run()
    } match {
      case Success(_) =>
        println(step.passedRunMsg)
        PassedStepExecution(step.name)
      case Failure(ex) =>
        println(step.failedRunMsg)
        FailedStepExecution(step.name, ex)
    }
  }

  private[model] def executeRouterStep[T <: StepX with Router](step: T): StepExecution =
    step.route().execute() match {
      case x: PassedStepExecution =>
        PassedStepExecution(step.name, List(x))
      case x: StepExecution =>
        FailedStepExecution(step.name, RouterStepFailed, List(x))
    }

  private[model] def executeSerialStep[T <: StepX with Serial](step: T): StepExecution = {
    val executionProcess = lift[StepX with StepShape, StepExecution](_.execute()).flatMap {
      case passedExecution: PassedStepExecution =>
        emit[StepX with StepShape, StepExecution](passedExecution)
      case nonPassedExecution =>
        emit(nonPassedExecution) ++ lift(_.ignore)
    }

    executionProcess(step.toStream).toList
      .filter(_.isSuccess)
      .map(_.get)
      .foldLeft[StepExecution](PassedStepExecution(step.name)) {
      case (acc, stepExecution) =>
        stepExecution match {
          case passedExecution: PassedStepExecution =>
            PassedStepExecution(step.name, acc.innerStepExecutions :+ passedExecution)
          case nonPassedExecution =>
            FailedStepExecution(step.name, SerialStepFailed, acc.innerStepExecutions :+ nonPassedExecution)
        }
    }
  }

  private[model] def ignoreStep(step: StepX): StepExecution =
    IgnoredStepExecution(step.name)

}
