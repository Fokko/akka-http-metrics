package backline.http.metrics

import akka.http.scaladsl.server.Directive0

@deprecated(
  "`StatusCodeMetrics` has been deprecated. Use `StatusCodeCounterDirectives`.",
  "0.6.0")
trait StatusCodeMetrics extends MetricsBase with StatusCodeCounterCommon {

  @deprecated(
    "`withStatusCodeCounter` has been deprecated. Use `StatusCodeCounterDirectives#withStatusCodeCounter`.",
    "0.6.0")
  def withStatusCodeCounter: Directive0 =
    responseCodes(ctx => getMetricName(ctx))

  @deprecated(
    "`withStatusCodeCounterNamed` has been deprecated. Use `StatusCodeCounterDirectives#withStatusCodeCounterNamed`.",
    "0.6.0")
  def withStatusCodeCounterNamed(name: String): Directive0 =
    responseCodes(_ => name)
}
