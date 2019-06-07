package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.ShapedStepsTest._
import org.scalatest.{FlatSpec, Matchers}

class ShapedStepsTest extends FlatSpec with Matchers {

  "SetupStep1 + SetupStep2" should "create Serial Setup step" in {
    // when
    val actual = SetupStep1 + SetupStep2

    val left = actual.left
    val right = actual.right.apply()

    // then
    left should be(SetupStep1)
    right should be(SetupStep2)
  }

  "TestStep1 + TestStep2" should "create Serial Test step" in {
    // when
    val actual = TestStep1 + TestStep2

    val left = actual.left
    val right = actual.right.apply()

    // then
    left should be(TestStep1)
    right should be(TestStep2)
  }

  "CleanupStep1 + CleanupStep2" should "create Serial Cleanup step" in {
    // when
    val actual = CleanupStep1 + CleanupStep2

    val left = actual.left
    val right = actual.right.apply()

    // then
    left should be(CleanupStep1)
    right should be(CleanupStep2)
  }

}

object ShapedStepsTest {

  trait DummyRunnable {

    def run(): Unit = {}

  }

  case object SetupStep1 extends ShapelessSetupStep with DummyRunnable {

    override def name: String = "My Setup Step 1"

  }

  case object SetupStep2 extends ShapelessSetupStep with DummyRunnable {

    override def name: String = "My Setup Step 2"

  }

  case object TestStep1 extends ShapelessTestStep with DummyRunnable {

    override def name: String = "My Test Step 1"

  }

  case object TestStep2 extends ShapelessTestStep with DummyRunnable {

    override def name: String = "My Test Step 2"

  }

  case object CleanupStep1 extends ShapelessCleanupStep with DummyRunnable {

    override def name: String = "My Cleanup Step 1"

  }

  case object CleanupStep2 extends ShapelessCleanupStep with DummyRunnable {

    override def name: String = "My Cleanup Step 2"

  }

}
