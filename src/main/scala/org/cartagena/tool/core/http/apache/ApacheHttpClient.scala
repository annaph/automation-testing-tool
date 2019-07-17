package org.cartagena.tool.core.http.apache

import org.apache.http.client.{HttpClient => Client}
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.protocol.{BasicHttpContext, HttpContext => Context}
import org.cartagena.tool.core.http.apache.ApacheHttpClientRefs._
import org.cartagena.tool.core.model.HttpNativeClientComponent
import scalaz.Forall
import scalaz.effect.ST
import scalaz.effect.ST.{returnST, runST}

import scala.util.{Failure, Success, Try}

trait ApacheHttpClient {

  def get: Client

  def context: Context

  def start(): Unit

  def reset(): Unit

  def close(): Unit

  def isUp: Boolean

}

trait ApacheHttpClientComponent extends HttpNativeClientComponent {

  private[core] val apacheHttpClient: ApacheHttpClient

}

class ApacheHttpClientImpl extends ApacheHttpClient {

  private[apache] val clientRef: ClientRef =
    ApacheHttpClientRefs.clientRef

  private[apache] val contextRef: ContextRef =
    ApacheHttpClientRefs.contextRef

  override def get: Client =
    ApacheHttpClientImpl.httpClient(clientRef)

  override def context: Context =
    ApacheHttpClientImpl.httpContext(contextRef)

  override def start(): Unit =
    ApacheHttpClientImpl.startHttpClient(clientRef, contextRef)

  override def reset(): Unit =
    ApacheHttpClientImpl.resetHttpClient(clientRef, contextRef)

  override def close(): Unit =
    ApacheHttpClientImpl.closeHttpClient(clientRef, contextRef)

  override def isUp: Boolean =
    Try(get).isSuccess

}

object ApacheHttpClientImpl {

  type ForallST[T] = Forall[({type λ[S] = ST[S, T]})#λ]

  type ReadRefAction[T] = Ref[T] => ST[Nothing, Try[T]]

  type UpdateRefsAction = (ClientRef, ContextRef) => ST[Nothing, Try[Unit]]

  private def httpClient(clientRef: ClientRef): Client =
    readRef(clientRef)

  private def httpContext(contextRef: ContextRef): Context =
    readRef(contextRef)

  private def startHttpClient(clientRef: ClientRef, contextRef: ContextRef): Unit =
    updateRefs(clientRef, contextRef)(startClientAndContextAction)

  private def resetHttpClient(clientRef: ClientRef, contextRef: ContextRef): Unit =
    updateRefs(clientRef, contextRef)(resetClientAndContextAction)

  private def closeHttpClient(clientRef: ClientRef, contextRef: ContextRef): Unit =
    updateRefs(clientRef, contextRef)(closeClientAndContextAction)

  private def readRef[T](ref: Ref[T]): T =
    runReadRefAction(ref)(readRefAction) match {
      case Success(obj) =>
        obj
      case Failure(e) =>
        throw e
    }

  private def readRefAction[T]: ReadRefAction[T] =
    ref =>
      for {
        refAsOption <- ref.read
        refAsTry <- returnST(toTry(refAsOption))
      } yield refAsTry

  private def updateRefs(clientRef: ClientRef, contextRef: ContextRef)(action: UpdateRefsAction): Unit =
    runUpdateRefsAction(clientRef, contextRef)(action) match {
      case Success(_) =>
      case Failure(e) =>
        throw e
    }

  private def startClientAndContextAction: UpdateRefsAction =
    (clientRef, contextRef) =>
      for {
        client <- readRefAction(clientRef)
        createdClientAndContext <-
        if (client.isSuccess) returnST(Failure(HttpClientIsUp)) else createRefsAction(clientRef, contextRef)
      } yield createdClientAndContext

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

  private def closeClientAndContextAction: UpdateRefsAction =
    (clientRef, contextRef) =>
      for {
        client <- readRefAction(clientRef)
        closedClientAndContext <-
        if (client.isFailure) returnST(Failure(HttpClientIsNotUp)) else destroyRefsAction(clientRef, contextRef)
      } yield closedClientAndContext

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

  private def toTry[T](obj: Option[T]): Try[T] =
    obj match {
      case Some(o) =>
        Success(o)
      case None =>
        Failure(HttpClientIsNotUp)
    }

}

case object HttpClientIsUp extends Exception("Apache Http client is up!")

case object HttpClientIsNotUp extends Exception("Apache Http client is NOT up!")
