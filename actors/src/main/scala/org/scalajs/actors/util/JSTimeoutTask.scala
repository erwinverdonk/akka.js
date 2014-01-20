package org.scalajs.actors
package util

import scala.scalajs.js

import scala.concurrent.duration.{Duration, FiniteDuration}

class JSTimeoutTask(delay: FiniteDuration, task: => Any) extends Cancellable {
  private[this] var underlying: js.Dynamic =
    js.Dynamic.global.setTimeout({ () =>
      underlying = null
      task
    }, delay.toMillis)

  def isCancelled: Boolean = underlying ne null

  def cancel(): Boolean = {
    if (isCancelled) false
    else {
      js.Dynamic.global.clearTimeout(underlying)
      underlying = null
      true
    }
  }
}

object JSTimeoutTask {
  def apply(duration: FiniteDuration)(task: => Any): JSTimeoutTask =
    new JSTimeoutTask(duration, task)
}