package org.cartagena.tool.core

sealed trait Process[I, O] {

  def apply(s: Stream[O]): Stream[O] = this match {
    case Await(recv) => s match {
      case h #:: t => ???
      case xs => ???
    }
    case Emit(h, t) =>
      h #:: t(s)
    case Halt(End) =>
      Stream()
    case Halt(e) =>
      throw new Exception("Error running process!", e)
  }

}

case class Await[I, O](recv: Option[I] => Process[I, O]) extends Process[I, O]

case class Emit[I, O](head: O, tail: Process[I,O] = Halt(End)) extends Process[I, O]

case class Halt[I, O](err: Throwable = End) extends Process[I, O]

case object End extends Exception

case object Kill extends Exception

object Process {

}

class TestCaseProcess extends Process[Step, Step]

object TestCaseProcess {

}