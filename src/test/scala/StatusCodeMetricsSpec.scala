import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import backline.http.metrics.RouteSpecification
import com.codahale.metrics.MetricRegistry

import scala.concurrent.ExecutionContext

object StatusCodeMetricsSpec
    extends RouteSpecification
    with StatusCodeMetrics
    with Directives {
  val metricRegistry = new MetricRegistry()

  "count up for successful requests" in {
    (1 to 1000) foreach { _ =>
      Get("/ok") ~> routes ~> check {
        status === StatusCodes.OK
      }
    }
    val counts = metricRegistry.counter("ok.GET-2xx")
    counts.getCount() must be_==(1000).eventually
  }

  "count up for successful requests with custom name" in {
    (1 to 1000) foreach { _ =>
      Get("/ok") ~> routes2 ~> check {
        status === StatusCodes.OK
      }
    }
    val counts = metricRegistry.counter("other-name-2xx")
    counts.getCount() must be_==(1000).eventually
  }

  "count up for redirecting requests" in {
    (1 to 1000) foreach { _ =>
      Get("/redirect") ~> routes ~> check {
        status === StatusCodes.Found
      }
    }
    val counts = metricRegistry.counter("redirect.GET-3xx")
    counts.getCount() must be_==(1000).eventually
  }

  "count up for redirecting requests with custom name" in {
    (1 to 1000) foreach { _ =>
      Get("/redirect") ~> routes2 ~> check {
        status === StatusCodes.Found
      }
    }
    val counts = metricRegistry.counter("other-name-3xx")
    counts.getCount() must be_==(1000).eventually
  }

  "count up for bad requests" in {
    (1 to 1000) foreach { _ =>
      Get("/bad") ~> routes ~> check {
        status === StatusCodes.BadRequest
      }
    }
    val counts = metricRegistry.counter("bad.GET-4xx")
    counts.getCount() must be_==(1000).eventually
  }

  "count up for bad requests with custom name" in {
    (1 to 1000) foreach { _ =>
      Get("/bad") ~> routes2 ~> check {
        status === StatusCodes.BadRequest
      }
    }
    val counts = metricRegistry.counter("other-name-4xx")
    counts.getCount() must be_==(1000).eventually
  }

  "count up for failing requests" in {
    (1 to 1000) foreach { _ =>
      Get("/fail") ~> routes ~> check {
        status === StatusCodes.InternalServerError
      }
    }
    val counts = metricRegistry.counter("fail.GET-5xx")
    counts.getCount() must be_==(1000).eventually
  }

  "count up for failing requests with custom name" in {
    (1 to 1000) foreach { _ =>
      Get("/fail") ~> routes2 ~> check {
        status === StatusCodes.InternalServerError
      }
    }
    val counts = metricRegistry.counter("other-name-5xx")
    counts.getCount() must be_==(1000).eventually
  }

  def routes =
    withStatusCodeCounter {
      (get & path("ok")) {
        complete(StatusCodes.OK)
      } ~
        (get & path("redirect")) {
          redirect("/other", StatusCodes.Found)
        } ~
        (get & path("bad")) {
          complete(StatusCodes.BadRequest)
        } ~
        (get & path("fail")) {
          complete(StatusCodes.InternalServerError)
        }
    }

  def routes2 =
    withStatusCodeCounterNamed("other-name") {
      (get & path("ok")) {
        complete(StatusCodes.OK)
      } ~
        (get & path("redirect")) {
          redirect("/other", StatusCodes.Found)
        } ~
        (get & path("bad")) {
          complete(StatusCodes.BadRequest)
        } ~
        (get & path("fail")) {
          complete(StatusCodes.InternalServerError)
        }
    }
}
