package org.cartagena.tool.core

import org.cartagena.tool.core.model.Context

import scala.reflect.runtime.universe.TypeTag
import scala.util.{Failure, Success, Try}

sealed trait BuildStep

trait HasContext extends BuildStep

trait HasKey extends BuildStep

trait HasValue extends BuildStep

class ContextOperationsBuilder[T, S <: BuildStep] private(private var context: Context,
                                                          private var key: String,
                                                          private var value: T) {

  protected def this() =
    this(null, null, null.asInstanceOf[T])

  protected def this(builder: ContextOperationsBuilder[T, _]) =
    this(builder.context, builder.key, builder.value)

  def withContext(context: Context): ContextOperationsBuilder[T, HasContext] = {
    this.context = context
    new ContextOperationsBuilder[T, HasContext](this)
  }

  def withKey(key: String)(implicit ev: S =:= HasContext): ContextOperationsBuilder[T, HasKey] = {
    this.key = key
    new ContextOperationsBuilder[T, HasKey](this)
  }

  def withValue(value: T)(implicit ev: S =:= HasKey): ContextOperationsBuilder[T, HasValue] = {
    this.value = value
    new ContextOperationsBuilder[T, HasValue](this)
  }

  def get(implicit ev1: S =:= HasKey, ev2: TypeTag[T]): T =
    ContextOperationsBuilder.extractValue(context.get[T](key))

  def create()(implicit ev1: S =:= HasValue, ev2: TypeTag[T]): T =
    ContextOperationsBuilder.extractValue(context.create[T](key, value))

  def update()(implicit ev1: S =:= HasValue, ev2: TypeTag[T]): T =
    ContextOperationsBuilder.extractValue(context.update[T](key, value))

  def remove()(implicit ev1: S =:= HasKey, ev2: TypeTag[T]): T =
    ContextOperationsBuilder.extractValue(context.remove[T](key))

}

object ContextOperationsBuilder {

  def apply[T](): ContextOperationsBuilder[T, BuildStep] =
    new ContextOperationsBuilder[T, BuildStep]()

  private def extractValue[T](value: Try[T]): T =
    value match {
      case Success(v) =>
        v
      case Failure(e) =>
        throw e
    }

}
