package backline.http.metrics
import com.codahale.metrics._
import akka.http.scaladsl.server.{Directive0, RequestContext}
import scala.util.control.NonFatal

trait HttpMeterMetrics extends MetricsBase {
  def meterDirective: Directive0 =
    meter(ctx => getMetricName(ctx))

  def meterDirectiveWithName(name: String): Directive0 =
    meter(_ => name)

  private[this] def meter(nameFunc: RequestContext => String): Directive0 = {
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
