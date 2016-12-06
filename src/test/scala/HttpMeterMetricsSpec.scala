package backline.http.metrics
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.model.StatusCodes
import com.codahale.metrics.MetricRegistry
import scala.concurrent.duration._

object HttpMeterMetricsSpec extends RouteSpecification with HttpMeterMetrics with Directives {
  val metricRegistry = new MetricRegistry()

  "record rates for /ping" in {
    def routes =
      meterDirective {
        (get & path("ping")) {
          complete("pong")
        }
      }

    (1 to 60) foreach { _ =>
      Get("/ping") ~> routes ~> check {
        status === StatusCodes.OK
        responseAs[String].contains("pong") must beTrue
      }
    }

    val meter = metricRegistry.meter("ping.GET")
    meter.getCount must be_==(60)
    meter.getMeanRate must beGreaterThan(60D)
    meter.getMeanRate must beLessThan(61D).eventually(5, 1000 millis)
    meter.getOneMinuteRate must beGreaterThan(1D).eventually(5, 1000 millis)
  }

  "work with custom names" in {
    def routes =
      meterDirectiveWithName("ping-route") {
        (get & path("ping")) {
          complete("pong")
        }
      }

    (1 to 100) foreach { _ =>
      Get("/ping") ~> routes ~> check {
        status === StatusCodes.OK
        responseAs[String].contains("pong") must beTrue
      }
    }

    val meter = metricRegistry.meter("ping-route")
    meter.getCount must be_==(100)
    meter.getMeanRate must beGreaterThan(100D)
    meter.getMeanRate must beLessThan(101D).eventually(5, 1000 millis)
    meter.getOneMinuteRate must beGreaterThan(1D).eventually(5, 1000 millis)
  }
}
