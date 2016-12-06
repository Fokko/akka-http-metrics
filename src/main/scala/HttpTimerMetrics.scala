package backline.http.metrics
import com.codahale.metrics._
import akka.http.scaladsl.server.{Directive0, RequestContext}
import scala.util.control.NonFatal
import scala.util.Failure

trait HttpTimerMetrics extends MetricsBase {
  def timerDirective: Directive0 =
    timer(ctx => getMetricName(ctx))

  def timerDirectiveWithName(name: String): Directive0 =
    timer(_ => name)

  private[this] def timer(nameFunc: RequestContext => String): Directive0 = {
    import scala.concurrent.ExecutionContext.Implicits.global
    mapRouteResultFuture { fut =>
      // nameFunc(fut)
      val timer = findAndRegisterTimer("ping-fut.GET").time()
      try {
        fut
      } catch {
        case NonFatal(err) =>
          timer.stop()
          Failure(err)
      }
      fut.onComplete {
        case _ =>
          timer.stop()
      }
      fut
    }
  }
}
