
name := "akka-http-metrics"

organization := "backline"

version := "1.0.0"

val akkaVersion = "10.1.3"

libraryDependencies ++= Seq(
  "io.dropwizard.metrics" % "metrics-core" % "3.1.2",
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaVersion,
  // Tests
  "org.specs2" %% "specs2-core" % "3.8.6" % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

bintrayOrganization in bintray := Some("backline")

bintrayRepository := "open-source"

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))
