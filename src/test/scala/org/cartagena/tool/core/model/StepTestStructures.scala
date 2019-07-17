package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.StepDimensions.StepShape
import org.cartagena.tool.core.model.StepExtensions.UnsupportedRunnable

object StepTestStructures {

  case object MySetupStep extends ShapelessSetupStep {

    override def name: String = "My Setup Step"

    override def run(): Unit = {}

  }

  case object MySetupStepToFail extends ShapelessSetupStep {

    override def name: String = "My Setup Step to fail"

    override def run(): Unit =
      throw MyStepException

  }

  case object MyShapelessStep1 extends ShapelessTestStep {

    override val name: String = "My Shapeless Step"

    override def run(): Unit = {}

  }

  case object MyShapelessStepToFail1 extends ShapelessTestStep {

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

  case object MyShapelessStep3 extends ShapelessTestStep {

    override val name: String = "My Shapeless Step 3"

    override def run(): Unit = {}

  }

  case object MyShapelessStep3ToFail extends ShapelessTestStep {

    override val name: String = "My Shapeless Step 3 to fail"

    override def run(): Unit =
      throw MyStepException

  }

  case object MyRouterStep extends RouterTestStep {

    override val name: String = "My Router Step"

    override def route(): Step with StepShape =
      MyShapelessStep1

  }

  case object MyRouterStepToFail extends RouterTestStep {

    override val name: String = "My Router Step to fail"

    override def route(): Step with StepShape =
      MyShapelessStepToFail1

  }

  case object MyCleanupStep extends ShapelessCleanupStep {

    override def name: String = "My Cleanup Step"

    override def run(): Unit = {}

  }

  case object MyCleanupStepToFail extends ShapelessCleanupStep {

    override def name: String = "My Cleanup Step to fail"

    override def run(): Unit =
      throw MyStepException

  }

  case object MyStepShape extends ShapelessTestStep {

    override val name: String = "My Step Shape"

    override def run(): Unit = {}

    override private[model] def execute(): StepExecution =
      PassedStepExecution(name)

  }

  case object MyUnsupportedRunnableStep extends ShapelessTestStep with UnsupportedRunnable {

    override val name: String = "My unsupported runnable Step"

  }

  case object MyStepException extends Exception

}
