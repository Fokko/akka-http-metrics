package backline.http.metrics

import akka.http.scaladsl.server.{Directive0, RequestContext}
import scala.util.control.NonFatal

trait MeterDirectives extends MetricsBase {
  def withMeter: Directive0 =
    meter(ctx => getMetricName(ctx))

  def withMeterNamed(name: String): Directive0 =
    meter(_ => name)

  private[metrics] def meter(nameFunc: RequestContext => String): Directive0 = {
    mapInnerRoute { inner => ctx =>
      findAndRegisterMeter(nameFunc(ctx)).mark
      try {
        inner(ctx)
      } catch {
        case NonFatal(err) =>
          ctx.fail(err)
      }
    }
  }
}
