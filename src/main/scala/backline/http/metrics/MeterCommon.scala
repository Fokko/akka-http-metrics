package backline.http.metrics

import akka.http.scaladsl.server.{Directive0, RequestContext}

import scala.util.control.NonFatal

trait MeterCommon { self: MetricsBase =>
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
