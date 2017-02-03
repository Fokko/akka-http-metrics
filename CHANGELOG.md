### 0.5.0 (Unreleased)

CHANGES

`ResponseCodeMetrics` has been deprecated. A replacement of `StatusCodeMetrics` is recommended. This new interface does not require an `ExecutionContext` to be implicitly available. Thanks to [@notxcain](https://github.com/notxcain)

### 0.4.4 (Released 2017-02-03)

- Bump akka-http to `10.0.3`

### 0.4.3 (Released 2016-12-08)

- Cross compile to support Scala 2.11 and 2.12

### 0.4.2 (Released 2016-12-08)

- Fix timing for routes that have Futures

### 0.4.1 (Released 2016-12-06)

- Adding support for custom names in meter, timer, and response code metrics.

### 0.4.0 (Released 2016-12-02)

- Bump akka-http version to `10.0.0`
