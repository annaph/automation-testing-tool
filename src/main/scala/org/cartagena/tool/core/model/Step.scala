package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.Process.{emit, lift}
import org.cartagena.tool.core.model.StepDimensions.{Router, Serial, Shapeless, StepShape}
import org.cartagena.tool.core.model.StepExtensions.InfoMessages

import scala.util.{Failure, Success, Try}

trait Step {

  def name: String

  def profile: Profile

  def context: Context

  def run(): Unit

}

object Step {

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

  private[model] def executeShapelessStep[T <: Step with Shapeless with InfoMessages](step: T): StepExecution = {
    printStepSeparator()
    println(step.preRunMsg)

    Try {
      step.run()
    } match {
      case Success(_) =>
        println(step.passedRunMsg)
        printStepSeparator()

        PassedStepExecution(step.name)
      case Failure(ex) =>
        println(step.failedRunMsg)
        printStepSeparator()

        FailedStepExecution(step.name, ex)
    }
  }

  private def printStepSeparator(): Unit =
    println(StringBuilder.newBuilder.append("-") * 97)

  private[model] def executeRouterStep[T <: Step with Router](step: T): StepExecution =
    step.route().execute() match {
      case stepExecution: PassedStepExecution =>
        PassedStepExecution(step.name, stepExecution :: List.empty)
      case stepExecution: NonPassedStepExecution =>
        FailedStepExecution(step.name, RouterStepFailed, stepExecution :: List.empty)
    }

  private[model] def executeSerialStep[T <: Step with Serial](step: T): StepExecution = {
    val executionProcess = lift[Step with StepShape, StepExecution](_.execute()).flatMap {
      case stepExecution: PassedStepExecution =>
        emit[Step with StepShape, StepExecution](stepExecution)
      case stepExecution: NonPassedStepExecution =>
        emit[Step with StepShape, StepExecution](stepExecution) ++ lift(_.ignore)
    }

    executionProcess(step.toStream).toList
      .filter(_.isSuccess)
      .map(_.get)
      .foldLeft[StepExecution](PassedStepExecution(step.name)) {
      case (acc, stepExecution) =>
        stepExecution match {
          case _: PassedStepExecution =>
            PassedStepExecution(step.name, acc.innerStepExecutions :+ stepExecution)
          case _: NonPassedStepExecution =>
            FailedStepExecution(step.name, SerialStepFailed, acc.innerStepExecutions :+ stepExecution)
        }
    }
  }

  private[model] def ignoreStep(step: Step): StepExecution =
    IgnoredStepExecution(step.name)


}
