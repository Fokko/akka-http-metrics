package backline.http.metrics
import com.codahale.metrics._
import akka.http.scaladsl.server.{Directive0, RequestContext, RouteResult}
import akka.http.scaladsl.model.{StatusCodes, StatusCode}
import scala.util.control.NonFatal
import scala.concurrent.ExecutionContext

trait ResponseCodeMetrics extends MetricsBase {
  def responseCodeMetrics(implicit ec: ExecutionContext): Directive0 =
    responseCodes(ctx => getMetricName(ctx))

  def responseCodeMetricsWithName(name: String)(implicit ec: ExecutionContext): Directive0 =
    responseCodes(_ => name)

  private[this] def responseCodes(nameFunc: RequestContext => String)(implicit ec: ExecutionContext): Directive0 = {
    mapInnerRoute { inner => ctx =>
      try {
        val fut = inner(ctx)
        fut foreach {
          case RouteResult.Complete(resp) =>
            findAndRegisterCounter(s"${nameFunc(ctx)}-${liftStatusCode(resp.status)}").inc
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

  private[this] def liftStatusCode(code: StatusCode): String = code match {
    case c: StatusCodes.Informational         => "1xx"
    case c: StatusCodes.Success               => "2xx"
    case c: StatusCodes.Redirection           => "3xx"
    case c: StatusCodes.ClientError           => "4xx"
    case c: StatusCodes.ServerError           => "5xx"
    case StatusCodes.CustomStatusCode(custom) => custom.toString
  }
}
