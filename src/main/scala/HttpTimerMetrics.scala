package backline.http.metrics
import com.codahale.metrics._
import akka.http.scaladsl.server.{Directive0, RequestContext}
import scala.util.control.NonFatal

trait HttpTimerMetrics extends MetricsBase {
  def timerDirective: Directive0 =
    timer(ctx => getMetricName(ctx))

  def timerDirectiveWithName(name: String): Directive0 =
    timer(_ => name)

  private[this] def timer(nameFunc: RequestContext => String): Directive0 = {
    extractRequestContext.flatMap { ctx ⇒
      val timer = findAndRegisterTimer(nameFunc(ctx)).time()
      mapRouteResult { result ⇒
        timer.stop()
        result
      }
    }
  }
}
