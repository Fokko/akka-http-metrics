package backline.http.metrics

import akka.http.scaladsl.server.Directive0
import scala.concurrent.ExecutionContext

trait ResponseCodeMetrics extends MetricsBase
    with HttpResponseWrapping {

  def responseCodeMetrics(implicit ec: ExecutionContext): Directive0 =
    responseCodes(ctx => getMetricName(ctx))

  def responseCodeMetricsWithName(name: String)(implicit ec: ExecutionContext): Directive0 =
    responseCodes(_ => name)
}
