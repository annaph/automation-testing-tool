package org.cartagena.tool.core.model

import scala.util.{Failure, Success, Try}

sealed trait Context {

  def get[T: Manifest](key: String): T

  def create[T: Manifest](key: String, value: T): Unit

  def update[T: Manifest](key: String, value: T): Unit

  def remove[T: Manifest](key: String): Unit

}

case object EmptyContext extends Context {

  override def get[T: Manifest](key: String): T =
    throw new UnsupportedOperationException

  override def create[T: Manifest](key: String, value: T): Unit =
    throw new UnsupportedOperationException

  override def update[T: Manifest](key: String, value: T): Unit =
    throw new UnsupportedOperationException

  override def remove[T: Manifest](key: String): Unit =
    throw new UnsupportedOperationException

}

class SuiteContext extends Context {

  import scala.collection.mutable

  private val _entries = mutable.Map.empty[String, Any]

  override def get[T: Manifest](key: String): T =
    SuiteContext.get(this)(key)

  override def create[T: Manifest](key: String, value: T): Unit =
    SuiteContext.create(this)(key, value)

  override def update[T: Manifest](key: String, value: T): Unit =
    SuiteContext.update(this)(key, value)

  override def remove[T: Manifest](key: String): Unit =
    SuiteContext.remove(this)(key)

}

object SuiteContext {

  def apply(): SuiteContext = new SuiteContext

  def apply(product: Product): SuiteContext = {
    val context = new SuiteContext

    product.getClass.getDeclaredFields
      .map(_.getName)
      .zip(product.productIterator.toList)
      .toMap
      .foreach {
        case (key, value) =>
          context create(key, value)
      }

    context
  }

  private def get[T: Manifest](context: SuiteContext)(key: String): T =
    getEntry(context)(key) match {
      case Success((_, value)) =>
        value
      case Failure(e) =>
        throw e
    }

  private def create[T: Manifest](context: SuiteContext)(key: String, value: T): Unit =
    getEntry(context)(key) match {
      case Failure(x: KeyNotPresentException) =>
        context._entries put(key, value)
      case Failure(x: TypeNotMatchingException) =>
        throw x
      case _ =>
        throw new EntryExistsException(s"Entry with '$key' key and '${value.getClass}' type already exists!")
    }

  private def getEntry[T: Manifest](context: SuiteContext)(key: String): Try[(String, T)] =
    Try(context._entries get key)
      .flatMap(extractEntryValue(key, _))
      .flatMap(convertEntryValue(key, _))
      .map(key -> _)

  private def extractEntryValue(key: String, value: Option[Any]): Try[Any] =
    value match {
      case Some(x) =>
        Success(x)
      case None =>
        Failure(new KeyNotPresentException(s"No entry with '$key' key!"))
    }

  private def convertEntryValue[T: Manifest](key: String, value: Any): Try[T] =
    value match {
      case _ if value.getClass == implicitly[Manifest[T]].runtimeClass =>
        Success(value.asInstanceOf[T])
      case _ =>
        Failure(new TypeNotMatchingException(s"No entry with '$key' key and '${value.getClass}' type!!"))
    }

  private def update[T: Manifest](context: SuiteContext)(key: String, value: T): Unit =
    getEntry(context)(key)
      .map(_ => context._entries put(key, value))

  private def remove[T: Manifest](context: SuiteContext)(key: String): Unit =
    getEntry(context)(key)
      .map(_ => context._entries remove key)

  class KeyPresentException(str: String) extends Exception(str)

  class KeyNotPresentException(str: String) extends Exception(str)

  class TypeNotMatchingException(str: String) extends Exception(str)

  class EntryExistsException(str: String) extends Exception(str)

}
