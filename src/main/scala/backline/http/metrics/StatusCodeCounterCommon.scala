package backline.http.metrics

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.{Directive0, RequestContext, RouteResult}

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

//TODO: In 0.7.0 remove `StatusCodeMetrics` and move this to `StatusCodeCounterDirectives`

trait StatusCodeCounterCommon { this: MetricsBase =>

  protected[metrics] def responseCodes(
      nameFunc: RequestContext => String): Directive0 = {
    extractExecutionContext.flatMap {
      implicit executionContext: ExecutionContext =>
        mapInnerRoute { inner => ctx =>
          try {
            val fut = inner(ctx)
            fut foreach {
              case RouteResult.Complete(resp) =>
                findAndRegisterCounter(
                  s"${nameFunc(ctx)}-${liftStatusCode(resp.status)}").inc
              case RouteResult.Rejected(_) =>
                findAndRegisterCounter(s"${nameFunc(ctx)}-rejections").inc
            }
            fut
          } catch {
            case NonFatal(err) =>
              findAndRegisterCounter(s"${nameFunc(ctx)}-failures").inc
              ctx.fail(err)
          }
        }
    }
  }

  protected[metrics] def liftStatusCode(code: StatusCode): String =
    code match {
      case c: StatusCodes.Informational => "1xx"
      case c: StatusCodes.Success => "2xx"
      case c: StatusCodes.Redirection => "3xx"
      case c: StatusCodes.ClientError => "4xx"
      case c: StatusCodes.ServerError => "5xx"
      case StatusCodes.CustomStatusCode(custom) => custom.toString
    }
}
