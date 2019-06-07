package org.cartagena.tool.core.http.apache

import org.apache.http.client.{HttpClient => Client}
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.protocol.{BasicHttpContext, HttpContext => Context}
import org.cartagena.tool.core.http.apache.ApacheHttpClientRefs.{ClientRef, ContextRef, Ref}
import org.cartagena.tool.core.model.HttpNativeClient
import scalaz.Forall
import scalaz.effect.ST
import scalaz.effect.ST.{returnST, runST}

import scala.util.{Failure, Success, Try}

trait ApacheHttpClient extends HttpNativeClient {

  private[apache] val clientRef: ClientRef =
    ApacheHttpClientRefs.clientRef

  private[apache] val contextRef: ContextRef =
    ApacheHttpClientRefs.contextRef

  private[apache] def httpContext: Context =
    ApacheHttpClient.httpContext(contextRef)

  private[apache] def isHttpClientUp: Boolean =
    Try(httpClient).isSuccess

  private[apache] def httpClient: Client =
    ApacheHttpClient.httpClient(clientRef)

  private[apache] def startHttpClient(): Unit =
    ApacheHttpClient.startHttpClient(clientRef, contextRef)

  private[apache] def closeHttpClient(): Unit =
    ApacheHttpClient.closeHttpClient(clientRef, contextRef)

  private[apache] def resetHttpClient(): Unit =
    ApacheHttpClient.resetHttpClient(clientRef, contextRef)

}

object ApacheHttpClient {

  type ForallST[T] = Forall[({type λ[S] = ST[S, T]})#λ]

  type ReadRefAction[T] = Ref[T] => ST[Nothing, Try[T]]

  type UpdateRefsAction = (ClientRef, ContextRef) => ST[Nothing, Try[Unit]]

  private def httpClient(clientRef: ClientRef): Client =
    readRef(clientRef)

  private def httpContext(contextRef: ContextRef): Context =
    readRef(contextRef)

  private def startHttpClient(clientRef: ClientRef, contextRef: ContextRef): Unit =
    updateRefs(clientRef, contextRef)(startClientAndContextAction)

  private def closeHttpClient(clientRef: ClientRef, contextRef: ContextRef): Unit =
    updateRefs(clientRef, contextRef)(closeClientAndContextAction)

  private def resetHttpClient(clientRef: ClientRef, contextRef: ContextRef): Unit =
    updateRefs(clientRef, contextRef)(resetClientAndContextAction)

  private def readRef[T](ref: Ref[T]): T =
    runReadRefAction(ref)(readRefAction) match {
      case Success(obj) =>
        obj
      case Failure(e) =>
        throw e
    }

  private def updateRefs(clientRef: ClientRef, contextRef: ContextRef)(action: UpdateRefsAction): Unit =
    runUpdateRefsAction(clientRef, contextRef)(action) match {
      case Success(_) =>
      case Failure(e) =>
        throw e
    }

  private def runReadRefAction[T](ref: Ref[T])(action: ReadRefAction[T]): Try[T] =
    runST(new ForallST[Try[T]] {
      override def apply[S]: ST[S, Try[T]] =
        action(ref).asInstanceOf[ST[S, Try[T]]]
    })

  private def runUpdateRefsAction(clientRef: ClientRef, contextRef: ContextRef)(action: UpdateRefsAction): Try[Unit] =
    runST(new ForallST[Try[Unit]] {
      override def apply[S]: ST[S, Try[Unit]] =
        action(clientRef, contextRef).asInstanceOf[ST[S, Try[Unit]]]
    })

  private def readRefAction[T]: ReadRefAction[T] =
    ref =>
      for {
        refAsOption <- ref.read
        refAsTry <- returnST(toTry(refAsOption))
      } yield refAsTry

  private def startClientAndContextAction: UpdateRefsAction =
    (clientRef, contextRef) =>
      for {
        client <- readRefAction(clientRef)
        createdClientAndContext <-
          if (client.isSuccess) returnST(Failure(HttpClientIsUp)) else createRefsAction(clientRef, contextRef)
      } yield createdClientAndContext

  private def closeClientAndContextAction: UpdateRefsAction =
    (clientRef, contextRef) =>
      for {
        client <- readRefAction(clientRef)
        closedClientAndContext <-
          if (client.isFailure) returnST(Failure(HttpClientIsNotUp)) else destroyRefsAction(clientRef, contextRef)
      } yield closedClientAndContext

  private def resetClientAndContextAction: UpdateRefsAction =
    (clientRef, contextRef) => {
      for {
        closedClientAndContext <- closeClientAndContextAction(clientRef, contextRef)
        restartedClientAndContext <-
          if (closedClientAndContext.isFailure) {
            returnST(Failure(HttpClientIsNotUp))
          } else {
            startClientAndContextAction(clientRef, contextRef)
          }
      } yield restartedClientAndContext
    }

  private def createRefsAction: UpdateRefsAction =
    (clientRef, context) =>
      for {
        _ <- clientRef write Some(HttpClientBuilder.create().build())
        _ <- context write Some(new BasicHttpContext())
      } yield Success(())

  private def destroyRefsAction: UpdateRefsAction =
    (clientRef, context) =>
      for {
        _ <- clientRef write None
        _ <- context write None
      } yield Success(())

  private def toTry[T](obj: Option[T]): Try[T] =
    obj match {
      case Some(o) =>
        Success(o)
      case None =>
        Failure(HttpClientIsNotUp)
    }

  case object HttpClientIsUp extends Exception("Apache Http client is up!")

  case object HttpClientIsNotUp extends Exception("Apache Http client is NOT up!")

}
