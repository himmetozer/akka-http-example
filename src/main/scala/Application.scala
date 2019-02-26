import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import controllers.{HeartbeatController, TicketController}
import repositories.{DBContext, TicketRepo}
import services.TicketService

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Application extends App {

  implicit val system = ActorSystem("ticketServer")
  implicit val materializer = ActorMaterializer()
  implicit val executor: ExecutionContext = system.dispatcher

  lazy val config = ConfigFactory.load()
  lazy val db = new DBContext(config)
  val ticketRepo = new TicketRepo(db)
  val ticketService = new TicketService(ticketRepo)
  val ticketController = new TicketController(ticketService)
  val heartbeatController = new HeartbeatController()

  val routes = ticketController.route

  val servers = for {
    server <- Http().bindAndHandle(routes, "0.0.0.0", 8080)
    adminServer <- Http().bindAndHandle(heartbeatController.route, "0.0.0.0", 9000)
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
