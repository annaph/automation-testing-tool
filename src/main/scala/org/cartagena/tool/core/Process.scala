package org.cartagena.tool.core

import scala.util.{Failure, Success, Try}

sealed trait Process[I, O] {

  def apply(s: Stream[I]): Stream[Try[O]] =
    Process.apply(this, s)

  def unit(o: O): Process[I, O] =
    Process.emit(o)

  def map[O2](f: O => O2): Process[I, O2] =
    Process.map(this)(f)

  def flatMap[O2](f: O => Process[I, O2]): Process[I, O2] =
    Process.flatMap(this)(f)

  def repeat: Process[I, O] =
    Process.repeat(this)

  def onHalt(f: Throwable => Process[I, O]): Process[I, O] =
    Process.onHalt(this)(f)

  def |>[O2](p2: Process[O, O2]): Process[I, O2] =
    pipe(p2)

  def pipe[O2](p2: Process[O, O2]): Process[I, O2] =
    Process.pipe(this, p2)

  def append(p: Process[I, O]): Process[I, O] =
    Process.append(this, p)

  def ++(p: Process[I, O]): Process[I, O] =
    append(p)

}

case class Await[I, O](recv: Option[I] => Process[I, O]) extends Process[I, O]

case class Emit[I, O](head: O, tail: Process[I, O] = Halt[I, O](End)) extends Process[I, O]

case class Halt[I, O](err: Throwable) extends Process[I, O]

case object End extends Exception

case object Kill extends Exception

object Process {

  def apply[I, O](p: Process[I, O], s: Stream[I]): Stream[Try[O]] = p match {
    case Await(recv) => s match {
      case h #:: t =>
        `try`(recv(Some(h)))(t)
      case xs =>
        `try`(recv(None))(xs)
    }
    case Emit(h, t) =>
      Success(h) #:: t(s)
    case Halt(End | Kill) =>
      Stream.empty
    case Halt(err) =>
      Failure(err) #:: Stream.empty[Try[O]]
  }

  def await[I, O](recv: Option[I] => Process[I, O]): Process[I, O] =
    Await(recv)

  def emit[I, O](head: O, tail: Process[I, O] = Halt[I, O](End)): Process[I, O] =
    Emit(head, tail)

  def halt[I, O]: Process[I, O] =
    Halt(End)

  def halt[I, O](err: Throwable): Process[I, O] =
    Halt(err)

  def unit[I, O](o: O): Process[I, O] =
    emit(o)

  def map[I, O, O2](p: Process[I, O])(f: O => O2): Process[I, O2] =
    p |> lift(f)

  def flatMap[I, O, O2](p: Process[I, O])(f: O => Process[I, O2]): Process[I, O2] = p match {
    case Await(recv) =>
      await(recv andThen (_ flatMap f))
    case Emit(h, t) =>
      `try`(f(h)) ++ t.flatMap(f)
    case Halt(err) =>
      halt(err)
  }

  def `try`[I, O](p: => Process[I, O]): Process[I, O] =
    Try(p) match {
      case Success(_) =>
        p
      case Failure(err) =>
        halt(err)
    }

  def repeat[I, O](p: Process[I, O]): Process[I, O] = {
    def go(pr: Process[I, O]): Process[I, O] = pr match {
      case Await(recv) => await {
        case None =>
          recv(None)
        case i =>
          go(recv(i))
      }
      case Emit(h, t) =>
        emit(h, go(t))
      case Halt(_) =>
        go(p)
    }

    go(p)
  }

  def onHalt[I, O](p: Process[I, O])(f: Throwable => Process[I, O]): Process[I, O] = p match {
    case Await(recv) =>
      await(recv andThen (_ onHalt f))
    case Emit(h, t) =>
      emit(h, t onHalt f)
    case Halt(e) =>
      `try`(f(e))
  }

  def pipe[I, O, O2](p1: Process[I, O], p2: Process[O, O2]): Process[I, O2] = {
    def go(pr1: Process[I, O], pr2: Process[O, O2]): Process[I, O2] = pr1 match {
      case Await(recv1) => pr2 match {
        case Await(_) => await[I, O2] {
          case Some(i) =>
            go(recv1(Some(i)), pr2)
          case _ =>
            halt
        }
        case Emit(h2, t2) =>
          emit(h2, go(pr1, t2))
        case Halt(_) =>
          halt
      }
      case Emit(h1, t1) => pr2 match {
        case Await(recv2) =>
          go(t1, recv2(Some(h1)))
        case Emit(h2, t2) =>
          emit(h2, go(t1, t2))
        case Halt(_) =>
          halt
      }
      case Halt(_) =>
        halt
    }

    go(p1, p2)
  }

  def append[I, O](p1: Process[I, O], p2: Process[I, O]): Process[I, O] =
    p1.onHalt {
      case End =>
        p2
      case err =>
        halt(err)
    }

  def liftOne[I, O](f: I => O): Process[I, O] = await[I, O] {
    case Some(i) =>
      emit(f(i))
    case None =>
      halt
  }

  def lift[I, O](f: I => O): Process[I, O] =
    liftOne(f).repeat

  def filter[I](p: I => Boolean): Process[I, I] = await[I, I] {
    case Some(i) if p(i) =>
      emit(i)
    case _ =>
      halt
  }.repeat

  def take[I](n: Int): Process[I, I] = await[I, I] {
    case Some(i) if n > 1 =>
      emit(i, take(n - 1))
    case Some(i) if n == 1 =>
      emit(i)
    case _ =>
      halt
  }

  def drop[I](n: Int): Process[I, I] = await[I, I] {
    case Some(i) if n > 0 =>
      drop(n - 1)
    case Some(i) if n == 0 =>
      emit(i, drop(0))
    case _ =>
      halt
  }

  def takeWhile[I](f: I => Boolean): Process[I, I] = await[I, I] {
    case Some(i) if f(i) =>
      Emit(i, takeWhile(f))
    case _ =>
      halt
  }

  def dropWhile[I](f: I => Boolean): Process[I, I] = await[I, I] {
    case Some(i) if f(i) =>
      dropWhile(f)
    case Some(i) =>
      emit(i, dropWhile(f))
    case _ =>
      halt
  }

  def count[I]: Process[I, Int] = {
    def go(c: Int): Process[I, Int] = await[I, Int] {
      case Some(_) =>
        emit(c, go(c + 1))
      case _ =>
        halt
    }

    go(1)
  }

  def zipWithIndex[I]: Process[I, (I, Int)] = {
    def go(c: Int): Process[I, (I, Int)] = await[I, (I, Int)] {
      case Some(i) =>
        emit(c).flatMap { index =>
          emit[I, (I, Int)](i -> index, go(c + 1))
        }
      case _ =>
        halt
    }

    go(0)
  }

  def exists[I](f: I => Boolean): Process[I, Boolean] = await[I, Boolean] {
    case Some(i) if !f(i) =>
      emit(false, exists(f))
    case Some(i) if f(i) =>
      emit(true)
    case _ =>
      halt
  }

}
