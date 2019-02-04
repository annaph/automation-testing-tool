package org.cartagena.tool.core

sealed trait Process[I, O] {

  def apply(s: Stream[I]): Stream[O] = this match {
    case Await(recv) => s match {
      case h #:: t =>
        recv(Some(h))(t)
      case xs =>
        recv(None)(xs)
    }
    case Emit(h, t) =>
      h #:: t(s)
    case Halt() =>
      Stream.empty
  }

  def repeat: Process[I, O] = {
    def go(p: Process[I, O]): Process[I, O] = p match {
      case Await(recv) => Await {
        case None =>
          recv(None)
        case i =>
          go(recv(i))
      }
      case Emit(h, t) =>
        Emit(h, go(t))
      case Halt() =>
        go(this)
    }

    go(this)
  }

  def |>[O2](p2: Process[O, O2]): Process[I, O2] =
    pipe(p2)

  def pipe[O2](p2: Process[O, O2]): Process[I, O2] =
    Process.pipe(this, p2)

  def map[O2](f: O => O2): Process[I, O2] =
    this |> Process.lift(f)

}

case class Await[I, O](recv: Option[I] => Process[I, O]) extends Process[I, O]

case class Emit[I, O](head: O, tail: Process[I, O] = Halt[I, O]()) extends Process[I, O]

case class Halt[I, O]() extends Process[I, O]

case object End extends Exception

case object Kill extends Exception

object Process {

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
        case Halt() =>
          halt
      }

      case Emit(h1, t1) => pr2 match {
        case Await(recv2) =>
          go(t1, recv2(Some(h1)))
        case Emit(h2, t2) =>
          emit(h2, go(t1, t2))
        case Halt() =>
          halt
      }

      case Halt() =>
        halt
    }

    go(p1, p2)
  }

  def lift[I, O](f: I => O): Process[I, O] = await[I, O] {
    case Some(i) =>
      emit(f(i))
    case None =>
      halt
  }.repeat

  def await[I, O](recv: Option[I] => Process[I, O]): Process[I, O] =
    Await(recv)

  def emit[I, O](head: O, tail: Process[I, O] = Halt[I, O]()): Process[I, O] =
    Emit(head, tail)

  def halt[I, O]: Process[I, O] =
    Halt()

  def filter[I](p: I => Boolean): Process[I, I] = await[I, I] {
    case Some(i) if p(i) =>
      emit(i)
    case _ =>
      halt
  }.repeat

  def take[I](n: Int): Process[I, I] = await[I, I] {
    case Some(i) if n > 0 =>
      emit(i, take(n - 1))
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
      case Some(i) =>
        emit(c, go(c + 1))
      case _ =>
        halt
    }

    go(1)
  }

}
