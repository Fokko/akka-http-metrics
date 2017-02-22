package backline.http.metrics

import akka.http.scaladsl.server.Directive0

trait HttpMeterMetrics extends MetricsBase with MeterCommon {
  @deprecated(
    "`meterDirective` has been deprecated. Switch to `withMeter` in `MeterDirectives`",
    "0.7.0")
  def meterDirective: Directive0 =
    meter(ctx => getMetricName(ctx))

  @deprecated(
    "`meterDirectiveWithName` has been deprecated. Switch to `withMeterNamed` in `MeterDirectives`",
    "0.7.0")
  def meterDirectiveWithName(name: String): Directive0 =
    meter(_ => name)
}
