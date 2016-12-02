# akka-http-metrics

![](https://api.travis-ci.org/Backline/akka-http-metrics.svg)

> A metrics wrapper around akka-http routes and directives.

Current versions:

- `akka-http-*`: `10.0.0`
- `metrics-core`: `3.1.2`

## Install & Usage

```scala
resolvers += "bintray-backline-open-source-releases" at "https://dl.bintray.com/backline/open-source"
libraryDependencies ++= Seq(
  "backline" %% "akka-http-metrics" % "0.4.0"
)
```

## Example Usage

```scala
import akka.http.scaladsl.server.Directives
import backline.http.metrics.HttpTimerMetrics

trait MyRoute extends Directives {
  def myRoute = {
    timerDirective {
      (get & path("ping")) {
        complete("pong")
      }
    }
  }
}
```

## Contributing

- If you want you can make a github issue or reach out to me directly.
- Open a PR and submit it!

## License

This software is licensed under the [Apache 2.0 license](LICENSE).
