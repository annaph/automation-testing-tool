package org.cartagena.tool.core.model

import org.cartagena.tool.core.model.SuiteContextX.EntriesRef
import scalaz.effect.STRef

import scala.collection.mutable
import scala.reflect.runtime.universe.typeTag

object SuiteContextTestUtil {

  val KEY_1 = "key1"

  val KEY_2 = "key2"

  val VALUE_1 = "value1"

  val VALUE_2 = List(1)

  case object SuiteContextTest extends SuiteContextX {

    override private[model] val entriesRef: EntriesRef = STRef[Nothing](mutable.Map(
      KEY_1 -> (typeTag[String] -> VALUE_1),
      KEY_2 -> (typeTag[List[Int]] -> VALUE_2)))

  }

}
