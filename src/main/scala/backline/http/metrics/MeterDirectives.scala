package backline.http.metrics

import akka.http.scaladsl.server.Directive0

trait MeterDirectives extends MetricsBase with MeterCommon {
  def withMeter: Directive0 =
    meter(ctx => getMetricName(ctx))

  def withMeterNamed(name: String): Directive0 =
    meter(_ => name)
}
