package backline.http.metrics

import akka.http.scaladsl.server.{Directive0, RequestContext}

// TODO: Remove in 0.8.0 along with `HttpTimerMetrics`

trait TimerCommon { self: MetricsBase =>
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
