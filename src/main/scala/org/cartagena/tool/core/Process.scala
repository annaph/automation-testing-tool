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
      Stream()
  }

  def repeat: Process[I, O] = {
    def go(p: Process[I, O]): Process[I, O] = p match {
      case Halt() => go(this)
      case Await(recv) => ???
      case Emit(h, t) =>
        Emit(h, go(t))
    }

    go(this)
  }

}

case class Await[I, O](recv: Option[I] => Process[I, O]) extends Process[I, O]

case class Emit[I, O](head: O, tail: Process[I,O] = Halt[I, O]()) extends Process[I, O]

case class Halt[I, O]() extends Process[I, O]

case object End extends Exception

case object Kill extends Exception

object Process {

  def liftOne[I, O](f: I => O): Process[I, O] = Await{
    case Some(i) =>
      Emit(f(i))
    case None =>
      Halt()
  }

}
