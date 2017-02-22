package backline.http.metrics

import java.util.Iterator

import akka.http.scaladsl.server.RequestContext
import akka.http.scaladsl.server.directives.BasicDirectives
import com.codahale.metrics._

trait MetricsBase extends BasicDirectives {
  def metricRegistry: MetricRegistry

  protected def findAndRegisterTimer(name: String): Timer = {
    val found =
      metricRegistry.getTimers(new NameBasedMetricFilter(name)).values.iterator
    findAndRegisterMetric(name, new Timer(), found)
  }

  protected def findAndRegisterCounter(name: String): Counter = {
    val found = metricRegistry
      .getCounters(new NameBasedMetricFilter(name))
      .values
      .iterator
    findAndRegisterMetric(name, new Counter(), found)
  }

  protected def findAndRegisterMeter(name: String): Meter = {
    val found =
      metricRegistry.getMeters(new NameBasedMetricFilter(name)).values.iterator
    findAndRegisterMetric(name, new Meter(), found)
  }

  protected def getMetricName(ctx: RequestContext): String = {
    val methodName = ctx.request.method.name
    val routeName = ctx.request.uri.path.toString.drop(1).replaceAll("/", ".")
    s"${routeName}.${methodName}"
  }

  private[this] def findAndRegisterMetric[T <: Metric](
      name: String,
      metric: => T,
      found: Iterator[T]): T = {
    val m = metric
    try {
      if (found != null && found.hasNext()) {
        found.next()
      } else {
        metricRegistry.register(name, m)
      }
    } catch {
      case err: IllegalArgumentException
          if err.getMessage.contains("A metric named") =>
        m
    }
  }

  protected class NameBasedMetricFilter(needle: String) extends MetricFilter {
    def matches(name: String, metric: Metric): Boolean =
      name equalsIgnoreCase needle
  }
}
