package controllers
import java.io.StringWriter

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe._
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.dropwizard.DropwizardExports
import io.prometheus.client.exporter.common.TextFormat

class HeartbeatController() extends ControllerBase {

  private val p = getClass.getPackage
  private val name = Option(p.getImplementationTitle).getOrElse("Run with java -jar <jar_name>")
  private val version = Option(p.getImplementationVersion).getOrElse("Run with java -jar <jar_name>")

  init()

  val route = {
    path("status") {
      complete(JsonObject("name" -> name, "version" -> version))
    } ~ path("metrics") {
      complete {
        val sw = new StringWriter()
        TextFormat.write004(sw, CollectorRegistry.defaultRegistry.metricFamilySamples())
        sw.flush()
        sw.toString
      }
    }
  }

  def init(): Unit = {
    CollectorRegistry.defaultRegistry.register(new DropwizardExports(metricRegistry))
  }

}
