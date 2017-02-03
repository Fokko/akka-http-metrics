package backline.http.metrics

import akka.http.scaladsl.server.Directive0

trait StatusCodeMetrics extends MetricsBase
    with HttpResponseWrapping {

  def withStatusCodeCounter: Directive0 =
    responseCodes(ctx => getMetricName(ctx))

  def withStatusCodeCounterNamed(name: String): Directive0 =
    responseCodes(_ => name)
}
