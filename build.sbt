name := "service-ticket"

version := "0.1"

scalaVersion := "2.12.8"

val akkaHttp = "10.1.8"
val akka = "2.5.21"
val akkaHttpCirce = "1.20.1"
val logbackClassic = "1.2.3"
val circe = "0.9.3"
val quill = "3.1.0"
val metrics = "4.0.4"
val prometheusSimpleClient = "0.6.0"


libraryDependencies ++= Seq(
  "com.typesafe.akka"        %% "akka-http"              % akkaHttp,
  "com.typesafe.akka"        %% "akka-stream"            % akka,
  "com.typesafe.akka"        %% "akka-slf4j"             % akka,
  "de.heikoseeberger"        %% "akka-http-circe"        % akkaHttpCirce,
  "io.circe"                 %% "circe-generic"          % circe,
  "ch.qos.logback"           % "logback-classic"         % logbackClassic,
  "io.getquill"              %% "quill-async-postgres"   % quill,
  "nl.grons"                 %% "metrics4-scala"         % metrics,
  "io.prometheus"            % "simpleclient_dropwizard" % prometheusSimpleClient,
  "io.prometheus"            % "simpleclient_common"     % prometheusSimpleClient,
)
