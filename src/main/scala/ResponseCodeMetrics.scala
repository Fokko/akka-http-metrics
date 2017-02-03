package backline.http.metrics

import akka.http.scaladsl.server.Directive0
import scala.concurrent.ExecutionContext

trait ResponseCodeMetrics extends MetricsBase
    with HttpResponseWrapping {

  @deprecated("`responseCodeMetrics` has been deprecated. Switch to `withStatusCodeCounter` in `StatusCodeMetrics` which doesn't require an ExecutionContext", "0.6.0")
  def responseCodeMetrics(implicit ec: ExecutionContext): Directive0 =
    responseCodes(ctx => getMetricName(ctx))

  @deprecated("`responseCodeMetricsWithName` has been deprecated. Switch to `withStatusCodeCounterNamed` in `StatusCodeMetrics` which doesn't require an ExecutionContext", "0.6.0")
  def responseCodeMetricsWithName(name: String)(implicit ec: ExecutionContext): Directive0 =
    responseCodes(_ => name)
}
