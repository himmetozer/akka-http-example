import com.typesafe.config.ConfigFactory
import controllers.{AdminController, TicketController}
import repositories.{DBContext, TicketRepo}
import services.TicketService

import scala.concurrent.ExecutionContext

trait Components {
  implicit val ec: ExecutionContext

  lazy val config = ConfigFactory.load()
  lazy val db = new DBContext(config)
  lazy val ticketRepo = new TicketRepo(db)
  lazy val ticketService = new TicketService(ticketRepo)
  lazy val ticketController = new TicketController(ticketService)
  lazy val adminController = new AdminController()
}
