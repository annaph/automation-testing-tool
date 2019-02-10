package org.cartagena.tool

package object core {

  def getStepsFrom[S <: Step](step: => S): Stream[S] = {
    def go(currStep: Option[S]): Stream[S] = currStep match {
      case Some(s) =>
        s #:: go(s.nextStep.asInstanceOf[Option[S]])
      case None =>
        Stream.empty[S]
    }

    go(Some(step))
  }

}
