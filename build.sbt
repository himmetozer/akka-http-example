name := "service-ticket"

version := "0.1"

scalaVersion := "2.12.8"

val akkaHttp = "10.1.1"
val akka = "2.5.11"
val circe = "0.9.3"
val macwire = "2.3.0"
val quill = "2.4.2"
val metricsV = "4.0.1"
val prometheusSimpleclientV = "0.0.17"

libraryDependencies ++= Seq(
  "com.typesafe.akka"        %% "akka-http"              % akkaHttp,
  "com.typesafe.akka"        %% "akka-stream"            % akka,
  "com.typesafe.akka"        %% "akka-slf4j"             % akka,
  "de.heikoseeberger"        %% "akka-http-circe"        % "1.20.1",
  "io.circe"                 %% "circe-generic"          % circe,
  "com.softwaremill.macwire" %% "macros"                 % macwire,
  "com.softwaremill.macwire" %% "util"                   % macwire,
  "ch.qos.logback"           % "logback-classic"         % "1.2.3",
  "io.getquill"              %% "quill-async-postgres"   % quill,
  "nl.grons"                 %% "metrics4-scala"         % metricsV,
  "io.prometheus"            % "simpleclient_dropwizard" % prometheusSimpleclientV,
  "io.prometheus"            % "simpleclient_common"     % prometheusSimpleclientV,
)
