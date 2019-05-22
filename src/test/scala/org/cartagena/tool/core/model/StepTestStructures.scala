package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepDimensions.StepShape
import org.cartagena.tool.core.model.StepExtensions.UnsupportedRunnable

object StepTestStructures {

  case object MyStepShape extends ShapelessTestStep {

    override val name: String = "My Step Shape"

    override def run(): Unit = {}

    override private[model] def execute(): StepExecution =
      PassedStepExecution(name)

  }

  case object MyShapelessStep extends ShapelessTestStep {

    override val name: String = "My Shapeless Step"

    override def run(): Unit = {}

  }

  case object MyUnsupportedRunnableStep extends ShapelessTestStep with UnsupportedRunnable {

    override val name: String = "My unsupported runnable Step"

  }

  case object MyShapelessStepToFail extends ShapelessTestStep {

    override val name: String = "My Shapeless Step to fail"

    override def run(): Unit =
      throw MyStepException

  }

  case object MyShapelessStep2 extends ShapelessTestStep {

    override val name: String = "My Shapeless Step 2"

    override def run(): Unit = {}

  }

  case object MyShapelessStep2ToFail extends ShapelessTestStep {

    override val name: String = "My Shapeless Step 2 to fail"

    override def run(): Unit =
      throw MyStepException

  }

  case object MyRouterStep extends RouterTestStep {

    override val name: String = "My Router Step"

    override def route(): StepX with StepShape =
      MyShapelessStep

    override def run(): Unit = {}

  }

  case object MyRouterStepToFail extends RouterTestStep {

    override val name: String = "My Router Step to fail"

    override def route(): StepX with StepShape =
      MyShapelessStepToFail

    override def run(): Unit = {}

  }

  case object MyStepException extends Exception

}
