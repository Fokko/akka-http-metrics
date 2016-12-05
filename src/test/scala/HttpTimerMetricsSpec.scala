package backline.http.metrics
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.model.StatusCodes
import com.codahale.metrics.MetricRegistry
import java.util.concurrent.atomic.AtomicBoolean

object HttpTimerMetricsSpec extends RouteSpecification with HttpTimerMetrics with Directives {
  sequential

  val metricRegistry = new MetricRegistry()

  "record timings for /ping" in {
    (1 to 1000) foreach { _ =>
      Get("/ping") ~> routes ~> check {
        status === StatusCodes.OK
        responseAs[String].contains("pong") must beTrue
      }
    }
    val counts = metricRegistry.timer("ping.GET")
    counts.getCount() must be_==(1000).eventually
  }

  "work with custom names" in {
    (1 to 1000) foreach { _ =>
      Get("/ping3") ~> routes ~> check {
        status === StatusCodes.OK
        responseAs[String].contains("pong3") must beTrue
      }
    }
    val counts = metricRegistry.timer("other-ping")
    counts.getCount() must be_==(1000).eventually
  }

  "the slow route should take over a second" in {
    (1 to 5) foreach { _ =>
      Get("/slow") ~> routes ~> check {
        status === StatusCodes.OK
        responseAs[String].contains("awake") must beTrue
      }
    }
    val counts = metricRegistry.timer("slow.GET")
    counts.getCount() must be_==(5)
    scala.math.abs(1D - counts.getMeanRate()) must be_<=(0.01D).eventually
  }

  "register under a bunch of threads" in {
    val passed = new AtomicBoolean(true)
    (1 to 1000) foreach { i =>
      (new Thread {
        override def run: Unit = {
          Thread.sleep(1000 - i)
          try {
            Get("/ping2") ~> routes ~> check {
              status === StatusCodes.OK
              responseAs[String].contains("pong2") must beTrue
            }
          } catch {
            case err: Throwable =>
              err.printStackTrace
              passed.set(false)
          }
        }
      }).start
    }
    val counts = metricRegistry.timer("ping2.GET")
    counts.getCount() must be_==(1000).eventually
    passed.get must beTrue
  }

  def routes =
    timerDirective {
      (get & path("ping")) {
        complete("pong")
      } ~
      (get & path("ping2")) {
        complete("pong2")
      } ~
      (get & path("slow")) {
        Thread.sleep(1000)
        complete("awake")
      }
    } ~
    timerDirectiveWithName("other-ping") {
      (get & path("ping3")) {
        complete("pong3")
      }
    }
}
