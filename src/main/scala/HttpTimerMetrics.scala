package backline.http.metrics

import com.codahale.metrics._
import akka.http.scaladsl.server.Directive0
import scala.util.control.NonFatal

trait HttpTimerMetrics extends MetricsBase {
  def timerDirective: Directive0 =
    createTimerDirective(None)

  def timerDirectiveFromReservoir(reservoir: Reservoir): Directive0 =
    createTimerDirective(Some(reservoir))

  private[this] def createTimerDirective(reservoir: Option[Reservoir]): Directive0 = {
    mapInnerRoute { inner => ctx =>
      val timer = findAndRegisterTimer(getMetricName(ctx), reservoir).time()
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
