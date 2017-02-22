package backline.http.metrics

import akka.http.scaladsl.testkit.{
  RouteTest,
  RouteTestTimeout,
  TestFrameworkInterface
}
import org.specs2.execute.{Failure, FailureException}
import org.specs2.mutable.Specification

import scala.concurrent.duration._

// until akka-http gets support
trait Specs2Interface extends TestFrameworkInterface {

  // from spray-testkit
  def failTest(msg: String): Nothing = {
    val trace = new Exception().getStackTrace.toList
    val fixedTrace =
      trace.drop(trace.indexWhere(_.getClassName.startsWith("org.specs2")) - 1)
    throw new FailureException(Failure(msg, stackTrace = fixedTrace))
  }
}

class RouteSpecification
    extends Specification
    with RouteTest
    with Specs2Interface { spec =>
  implicit def routeTestTimeout: RouteTestTimeout =
    RouteTestTimeout(FiniteDuration(5, "seconds"))
}
