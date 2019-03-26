package controllers

import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import models.JsonProtocol.Implicits.{ticketDataDecoder, ticketEncoder, ticketResultEncoder}
import models.{TicketData, TicketFilter}
import services.TicketService

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class TicketController(
    ticketService: TicketService
)(implicit ec: ExecutionContext)
    extends ControllerBase {

  import TicketController._
  import ticketService._

  val route = path("api" / "tickets") {
    (get & TicketFilter.get & withTimer(getTicketsTimer)) { filter =>
      onComplete(getTickets(filter)) {
        case Success(result) => Ok(result)
        case Failure(e)      => InternalServerError(e)
      }
    } ~ (post & entity(as[TicketData]) & withTimer(createTicketTimer)) { ticketData =>
      onComplete(createTicket(ticketData)) {
        case Success(result) => Created(result)
        case Failure(e)      => InternalServerError(e)
      }
    }
  } ~ path("api" / "tickets" / LongNumber) { id =>
    (get & withTimer(getTicketByIdTimer)) {
      onComplete(getTicket(id)) {
        case Success(Some(result)) => Ok(result)
        case Success(None)         => NotFound
        case Failure(e)            => InternalServerError(e)
      }
    } ~ (put & entity(as[TicketData]) & withTimer(updateTicketTimer)) { ticketData =>
      onComplete(updateTicket(id, ticketData)) {
        case Success(true)  => Ok()
        case Success(false) => NotFound
        case Failure(e)     => InternalServerError(e)
      }
    } ~ (delete & withTimer(deleteTicketTimer)) {
      onComplete(deleteTicket(id)) {
        case Success(true)  => Ok()
        case Success(false) => NotFound
        case Failure(e)     => InternalServerError(e)
      }
    }
  }

}

object TicketController {
  val getTicketsTimer = "controller_getTickets"
  val getTicketByIdTimer = "controller_getTicketById"
  val createTicketTimer = "controller_createTicket"
  val updateTicketTimer = "controller_updateTicket"
  val deleteTicketTimer = "controller_deleteTicket"
}
