package backline.http.metrics

import com.codahale.metrics._
import akka.http.scaladsl.server.Directive0
import scala.util.control.NonFatal

trait HttpTimerMetrics extends MetricsBase {
  def timerDirective(customReservoir: Option[Reservoir] = None): Directive0 = {
    mapInnerRoute { inner => ctx =>
      val timer = findAndRegisterTimer(getMetricName(ctx), customReservoir).time()
      try {
        inner(ctx)
      } catch {
        case NonFatal(err) =>
          ctx.fail(err)
      } finally {
        timer.stop()
      }
    }
  }
}
