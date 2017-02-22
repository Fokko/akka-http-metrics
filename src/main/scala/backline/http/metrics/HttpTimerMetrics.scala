package backline.http.metrics

import akka.http.scaladsl.server.Directive0

trait HttpTimerMetrics extends MetricsBase with TimerCommon {
  @deprecated(
    "`timerDirective` has been deprecated. Switch to `withTimer` in `TimerDirectives`",
    "0.7.0")
  def timerDirective: Directive0 =
    timer(ctx => getMetricName(ctx))

  @deprecated(
    "`timerDirectiveWithName` has been deprecated. Switch to `withTimerNamed` in `TimerDirectives`",
    "0.7.0")
  def timerDirectiveWithName(name: String): Directive0 =
    timer(_ => name)
}
