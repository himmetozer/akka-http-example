import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Application extends App with Components {

  implicit val system = ActorSystem("ticketServer")
  implicit val materializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  val routes = ticketController.route

  val servers = for {
    server <- Http().bindAndHandle(routes, "0.0.0.0", 8080)
    adminServer <- Http().bindAndHandle(adminController.route, "0.0.0.0", 9000)
  } yield {
    (server, adminServer)
  }

  servers.onComplete {
    case Success((server, adminServer)) =>
      system.log.info(s"server is up and running at ${server.localAddress.getHostName}:${server.localAddress.getPort}")
      system.log.info(s"admin server is up and running at ${adminServer.localAddress.getHostName}:${adminServer.localAddress.getPort}")
    case Failure(e) =>
      system.log.error(s"could not start application: {}", e.getMessage)
  }

}
