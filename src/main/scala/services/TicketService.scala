package services
import models.{Ticket, TicketData, TicketFilter, TicketResult}
import repositories.TicketRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TicketService(ticketRepo: TicketRepo) {

  def getTickets(filter: TicketFilter): Future[TicketResult] = {
    ticketRepo.get(filter.startId, filter.length + 1).map { tickets =>
      if (tickets.size == filter.length + 1) {
        TicketResult(tickets.lastOption.map(_.id), tickets.dropRight(1))
      } else {
        TicketResult(None, tickets)
      }
    }
  }

  def getTicket(id: Long): Future[Option[Ticket]] = {
    ticketRepo.getById(id)
  }

  def createTicket(ticketData: TicketData): Future[Long] = {
    ticketRepo.save(ticketData)
  }

  def updateTicket(id: Long, ticketData: TicketData): Future[Boolean] = {
    ticketRepo.update(id, ticketData).map(_ > 0)
  }

  def deleteTicket(id: Long): Future[Boolean] = {
    ticketRepo.delete(id).map(_ > 0)
  }

}
