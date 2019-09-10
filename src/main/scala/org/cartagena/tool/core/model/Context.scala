package org.cartagena.tool.core.model

import scalaz.Forall
import scalaz.effect.ST.{returnST, runST}
import scalaz.effect.{ST, STRef}

import scala.collection.mutable
import scala.reflect.runtime.universe.TypeTag
import scala.util.{Failure, Success, Try}

sealed trait Context {

  def get[T: TypeTag](key: String): Try[T]

  def create[T: TypeTag](key: String, value: T): Try[T]

  def update[T: TypeTag](key: String, value: T): Try[T]

  def remove[T: TypeTag](key: String): Try[T]

  def size: Int

  def isEmpty: Boolean =
    Context.isEmpty(this)

  def nonEmpty: Boolean =
    !isEmpty

}

object Context {

  def empty: Context =
    new SuiteContext

  def apply(): Context =
    empty

  def apply[T: TypeTag](entry: (String, T)): Context =
    entry match {
      case (key, value) =>
        val context = new SuiteContext
        context create(key, value)

        context
    }

  def apply[T1: TypeTag, T2: TypeTag](entry1: (String, T1), entry2: (String, T2)): Context =
    (entry1, entry2) match {
      case ((key1, value1), (key2, value2)) =>
        val context = new SuiteContext

        context create(key1, value1)
        context create(key2, value2)

        context
    }

  def apply[T1: TypeTag, T2: TypeTag, T3: TypeTag](entry1: (String, T1),
                                                   entry2: (String, T2),
                                                   entry3: (String, T3)): Context =
    (entry1, entry2, entry3) match {
      case ((key1, value1), (key2, value2), (key3, value3)) =>
        val context = new SuiteContext

        context create(key1, value1)
        context create(key2, value2)
        context create(key3, value3)

        context
    }

  private def isEmpty(context: Context): Boolean =
    if (context.size == 0) true else false

}

class SuiteContext extends Context {

  import SuiteContext.EntriesRef

  private[model] val entriesRef: EntriesRef = STRef[Nothing](mutable.Map.empty)

  override def get[T: TypeTag](key: String): Try[T] =
    SuiteContext.get(entriesRef, key)

  override def create[T: TypeTag](key: String, value: T): Try[T] =
    SuiteContext.create(entriesRef, key, value)

  override def update[T: TypeTag](key: String, value: T): Try[T] =
    SuiteContext.update(entriesRef, key, value)

  override def remove[T: TypeTag](key: String): Try[T] =
    SuiteContext.remove(entriesRef, key)

  override def size: Int =
    SuiteContext.size(entriesRef)

}

object SuiteContext {

  type ForallST[T] = Forall[({type λ[S] = ST[S, T]})#λ]

  type EntryValue = (TypeTag[_], Any)

  type Entries = mutable.Map[String, EntryValue]

  type EntriesRef = STRef[Nothing, Entries]

  type ReadEntryAction[T] = (EntriesRef, String) => ST[Nothing, Try[T]]

  type WriteEntryAction[T] = (EntriesRef, String, T) => ST[Nothing, Try[T]]

  type RemoveEntryAction[T] = (EntriesRef, String) => ST[Nothing, Try[T]]

  type SizeAction = EntriesRef => ST[Nothing, Int]

  private def get[T: TypeTag](entriesRef: EntriesRef, key: String): Try[T] =
    runReadEntryAction(entriesRef, key)(readEntryAction)

  private def create[T: TypeTag](entriesRef: EntriesRef, key: String, value: T): Try[T] =
    runWriteEntryAction(entriesRef, key, value)(writeEntryAction(createEntry[T]))

  private def update[T: TypeTag](entriesRef: EntriesRef, key: String, value: T): Try[T] =
    runWriteEntryAction(entriesRef, key, value)(writeEntryAction(updateEntry[T]))

  private def remove[T: TypeTag](entriesRef: EntriesRef, key: String): Try[T] =
    runRemoveEntryAction(entriesRef, key)(removeEntryAction())

  private def size(entriesRef: EntriesRef): Int =
    runSizeAction(entriesRef)(sizeAction())

  private def runReadEntryAction[T: TypeTag](entriesRef: EntriesRef, key: String)
                                            (action: ReadEntryAction[T]): Try[T] =
    runST(new ForallST[Try[T]] {
      override def apply[S]: ST[S, Try[T]] =
        action(entriesRef, key).asInstanceOf[ST[S, Try[T]]]
    })

  private def runWriteEntryAction[T: TypeTag](entriesRef: EntriesRef, key: String, value: T)
                                             (action: WriteEntryAction[T]): Try[T] =
    runST(new ForallST[Try[T]] {
      override def apply[S]: ST[S, Try[T]] =
        action(entriesRef, key, value).asInstanceOf[ST[S, Try[T]]]
    })

  private def runRemoveEntryAction[T: TypeTag](entriesRef: EntriesRef, key: String)
                                              (action: RemoveEntryAction[T]): Try[T] =
    runST(new ForallST[Try[T]] {
      override def apply[S]: ST[S, Try[T]] =
        action(entriesRef, key).asInstanceOf[ST[S, Try[T]]]
    })

  private def runSizeAction(entriesRef: EntriesRef)
                           (action: SizeAction): Int =
    runST(new ForallST[Int] {
      override def apply[S]: ST[S, Int] =
        action(entriesRef).asInstanceOf[ST[S, Int]]
    })

  private def readEntryAction[T: TypeTag]: ReadEntryAction[T] =
    (entriesRef, key) =>
      for {
        entries <- entriesRef.read
        value <- returnST(readEntry(entries, key))
      } yield value

  private def writeEntryAction[T: TypeTag](writeFunc: (Entries, String, T) => Try[Entries]): WriteEntryAction[T] =
    (entriesRef, key, value) =>
      for {
        entries <- entriesRef.read
        newEntries <- returnST[Nothing, Try[Entries]](writeFunc(entries, key, value))
        writtenValue <- newEntries match {
          case Success(map) =>
            for {
              updatedEntriesRef <- entriesRef write map
              updatedEntries <- updatedEntriesRef.read
            } yield Success[T](updatedEntries(key)._2.asInstanceOf[T])
          case Failure(e) =>
            returnST(Failure[T](e))
        }
      } yield writtenValue

  private def removeEntryAction[T: TypeTag](): RemoveEntryAction[T] =
    (entriesRef, key) =>
      for {
        entries <- entriesRef.read
        deletedValueAndNewEntries <- returnST[Nothing, Try[(T, Entries)]](removeEntry(entries, key))
        deletedValue <- deletedValueAndNewEntries match {
          case Success((t, map)) =>
            for {
              _ <- entriesRef write map
            } yield Success[T](t)
          case Failure(e) =>
            returnST(Failure[T](e))
        }
      } yield deletedValue

  private def sizeAction(): SizeAction =
    entriesRef =>
      for {
        entries <- entriesRef.read
      } yield entries.size

  private def readEntry[T: TypeTag](entries: mutable.Map[String, (TypeTag[_], Any)], key: String): Try[T] =
    entries get key match {
      case Some((tag, value)) if tag.tpe =:= implicitly[TypeTag[T]].tpe =>
        Success(value.asInstanceOf[T])
      case Some(_) =>
        Failure(new ValueTypeMismatchException(s"Incorrect value type for '$key' key specified!"))
      case None =>
        Failure(new KeyNotPresentException(s"No entry with '$key' key!"))
    }

  private def createEntry[T: TypeTag](entries: Entries, key: String, value: T): Try[Entries] =
    entries get key match {
      case Some(_) =>
        Failure(new KeyAlreadyPresentException(s"Entry with '$key' key already exists!"))
      case None =>
        entries put(key, implicitly[TypeTag[T]] -> value)
        Success(entries)
    }

  private def updateEntry[T: TypeTag](entries: Entries, key: String, value: T): Try[Entries] =
    entries get key match {
      case Some((tag, _)) if tag.tpe =:= implicitly[TypeTag[T]].tpe =>
        entries update(key, implicitly[TypeTag[T]] -> value)
        Success(entries)
      case Some(_) =>
        Failure(new ValueTypeMismatchException(s"Incorrect value type for '$key' key specified!"))
      case None =>
        Failure(new KeyNotPresentException(s"No entry with '$key' key!"))
    }

  private def removeEntry[T: TypeTag](entries: mutable.Map[String, (TypeTag[_], Any)], key: String): Try[(T, Entries)] =
    entries get key match {
      case Some((tag, value)) if tag.tpe =:= implicitly[TypeTag[T]].tpe =>
        entries remove key
        Success(value.asInstanceOf[T] -> entries)
      case Some(_) =>
        Failure(new ValueTypeMismatchException(s"Incorrect value type for '$key' key specified!"))
      case None =>
        Failure(new KeyNotPresentException(s"No entry with '$key' key!"))
    }

  class KeyNotPresentException(str: String) extends Exception(str)

  class KeyAlreadyPresentException(str: String) extends Exception(str)

  class ValueTypeMismatchException(str: String) extends Exception(str)

}
