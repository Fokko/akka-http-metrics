package backline.http.metrics

import akka.http.scaladsl.server.{Directive0, RequestContext}

trait TimerDirectives extends MetricsBase {
  def withTimer: Directive0 =
    timer(ctx => getMetricName(ctx))

  def withTimerNamed(name: String): Directive0 =
    timer(_ => name)

  private[metrics] def timer(nameFunc: RequestContext => String): Directive0 = {
    extractRequestContext.flatMap { ctx ⇒
      val timer = findAndRegisterTimer(nameFunc(ctx)).time()
      mapRouteResult { result ⇒
        timer.stop()
        result
      }
    }
  }
}
