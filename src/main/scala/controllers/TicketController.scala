package controllers

import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import models.JsonProtocol.Implicits.{ticketDataDecoder, ticketEncoder, ticketResultEncoder}
import models.{TicketData, TicketFilter}
import services.TicketService

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class TicketController(
    ticketService: TicketService
)(implicit ec: ExecutionContext, mat: Materializer)
    extends ControllerBase {

  import ticketService._

  val getTicketsTimer = metrics.timer("controller_getTickets")
  val getTicketByIdTimer = metrics.timer("controller_getTicketById")
  val createTicketTimer = metrics.timer("controller_createTicket")
  val updateTicketTimer = metrics.timer("controller_updateTicket")
  val deleteTicketTimer = metrics.timer("controller_deleteTicket")

  val route = {
    get {
      (path("api"/"v1"/"tickets") & TicketFilter.get & withTimer(getTicketsTimer)) { filter =>
        onComplete(getTickets(filter)) {
          case Success(result) => Ok(result)
          case Failure(e)      => InternalServerError(e)
        }
      } ~ (path("api"/"v1"/"tickets"/LongNumber) & withTimer(getTicketByIdTimer)) { id =>
        onComplete(getTicket(id)) {
          case Success(Some(result)) => Ok(result)
          case Success(None)         => NotFound
          case Failure(e)            => InternalServerError(e)
        }
      }
    } ~ post {
      (path("api"/"v1"/"tickets") & entity(as[TicketData]) & withTimer(createTicketTimer)) { ticketData =>
        onComplete(createTicket(ticketData)) {
          case Success(result) => Created(result)
          case Failure(e)      => InternalServerError(e)
        }
      }
    } ~ put {
      (path("api"/"v1"/"tickets"/LongNumber) & entity(as[TicketData]) & withTimer(updateTicketTimer)) {
        case (id, ticketData) =>
          onComplete(updateTicket(id, ticketData)) {
            case Success(true)  => Ok()
            case Success(false) => NotFound
            case Failure(e)     => InternalServerError(e)
          }
      }
    } ~ delete {
      (path("api"/"v1"/"tickets"/LongNumber) & withTimer(deleteTicketTimer)) { id =>
        onComplete(deleteTicket(id)) {
          case Success(true)  => Ok()
          case Success(false) => NotFound
          case Failure(e)     => InternalServerError(e)
        }
      }
    }
  }

}
