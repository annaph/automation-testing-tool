package org.cartagena.tool.core

package object model {

  def getStepsFrom[S <: Step](step: => S)(nextStep: S => S): Stream[S] = {
    def go(currStep: S): Stream[S] = currStep match {
      case NilStep =>
        Stream.empty[S]
      case _ =>
        currStep #:: go(nextStep(currStep))
    }

    go(step)
  }

}
