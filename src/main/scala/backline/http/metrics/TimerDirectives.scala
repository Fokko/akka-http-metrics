package backline.http.metrics

import akka.http.scaladsl.server.Directive0

trait TimerDirectives extends MetricsBase with TimerCommon {
  def withTimer: Directive0 =
    timer(ctx => getMetricName(ctx))

  def withTimerNamed(name: String): Directive0 =
    timer(_ => name)
}
