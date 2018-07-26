package io.github.antonshilov.domain.executor

import io.reactivex.Scheduler

/**
 * The interface to abstract the AndroidScheduler details from the domain layer,
 * but provide the ability for other layers to subscribe on that thread
 */
interface PostExecutionThread {
  /**
   * The RxJava [Scheduler] to be subscribed on by the use cases
   */
  val scheduler: Scheduler
}
