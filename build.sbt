import bintray.Keys._

name := "akka-http-metrics"

organization := "backline"

version := "0.4.4"

scalaVersion := "2.11.8"

crossScalaVersions := Seq(scalaVersion.value, "2.12.1")

val akkaVersion = "10.0.3"

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "io.dropwizard.metrics" % "metrics-core" % "3.1.2",
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaVersion,
  // Tests
  "org.specs2" %% "specs2-core" % "3.8.6" % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test"
)

resolvers ++= Seq(
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
)

scalacOptions in Test ++= Seq("-Yrangepos")

bintrayOrganization in bintray := Some("backline")

bintrayPublishSettings ++ Seq(
  publishArtifact in Test := false,
  repository in bintray := "open-source",
  homepage := Some(url("https://github.com/backline/akka-http-metrics")),
  licenses ++= Seq(("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")))
)
