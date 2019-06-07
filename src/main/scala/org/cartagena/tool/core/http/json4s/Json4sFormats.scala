package org.cartagena.tool.core.http.json4s

import org.cartagena.tool.core.http.json4s.Json4sFormatsRef.FormatsRef
import org.json4s.{Formats, Serializer}
import scalaz.Forall
import scalaz.effect.ST
import scalaz.effect.ST.{returnST, runST}

trait Json4sFormats {

  private[json4s] val formatsRef: FormatsRef =
    Json4sFormatsRef.formatsRef

  private[json4s] def formats: Formats =
    Json4sFormats.readFormats(formatsRef)

}

object Json4sFormats {

  type ForallST[T] = Forall[({type λ[S] = ST[S, T]})#λ]

  type Serializers = Iterable[Serializer[_]]

  type ReadFormatsRefAction = FormatsRef => ST[Nothing, Formats]

  type WriteFormatsRefAction = (FormatsRef, Formats) => ST[Nothing, Unit]

  type ModifyFormatsRefAction = (FormatsRef, Serializers) => ST[Nothing, Unit]

  private def readFormats(formatsRef: FormatsRef): Formats =
    runReadFormatsRefAction(formatsRef)(readFormatsRefAction)

  private[core] def setFormats(formatsRef: FormatsRef, formats: Formats): Unit =
    runWriteFormatsRefAction(formatsRef, formats)(writeFormatsAction)

  private[core] def addSerializers(formatsRef: FormatsRef, serializers: Serializers): Unit =
    runModifyFormatsRefAction(formatsRef, serializers)(modifyFormatsAction)

  private def runReadFormatsRefAction(formatsRef: FormatsRef)(action: ReadFormatsRefAction): Formats =
    runST(new ForallST[Formats] {
      override def apply[S]: ST[S, Formats] =
        action(formatsRef).asInstanceOf[ST[S, Formats]]
    })

  private def runWriteFormatsRefAction(formatsRef: FormatsRef, formats: Formats)(action: WriteFormatsRefAction): Unit =
    runST(new ForallST[Unit] {
      override def apply[S]: ST[S, Unit] =
        action(formatsRef, formats).asInstanceOf[ST[S, Unit]]
    })

  private def runModifyFormatsRefAction(formatsRef: FormatsRef, serializers: Serializers)
                                       (action: ModifyFormatsRefAction): Unit =
    runST(new ForallST[Unit] {
      override def apply[S]: ST[S, Unit] =
        action(formatsRef, serializers).asInstanceOf[ST[S, Unit]]
    })

  private def readFormatsRefAction: ReadFormatsRefAction =
    formatsRef =>
      for {
        formats <- formatsRef.read
      } yield formats

  private def writeFormatsAction: WriteFormatsRefAction =
    (formatsRef, formats) =>
      for {
        _ <- formatsRef write formats
      } yield ()

  private def modifyFormatsAction: ModifyFormatsRefAction =
    (formatsRef, serializers) =>
      for {
        formats <- readFormatsRefAction(formatsRef)
        updatedFormats <- returnST[Nothing, Formats](formats ++ serializers)
        _ <- writeFormatsAction(formatsRef, updatedFormats)
      } yield ()

}
