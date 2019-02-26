package repositories

import models.{Ticket, TicketData}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TicketRepo(dBContext: DBContext) {

  import dBContext._

  private val tickets = dBContext.quote(querySchema[Ticket]("ticket", _.id -> "id", _.title -> "title"))

  def get(maybeStartId: Option[Long], length: Int): Future[List[Ticket]] = {
    val query = maybeStartId match {
      case Some(startId) => quote(tickets.filter(_.id >= lift(startId)).take(length))
      case None => quote(tickets.take(length))
    }

    run(query)
  }

  def getById(id: Long): Future[Option[Ticket]] = {
    val query = quote(tickets.filter(_.id == lift(id)))

    run(query).map(_.headOption)
  }

  def save(ticketData: TicketData): Future[Long] = {
    val query = quote(tickets.insert(lift(ticketData.getTicket())).returning(_.id))

    run(query)
  }

  def update(id: Long, ticketData: TicketData): Future[Long] = {
    val ticket = ticketData.getTicket(id)
    val query = quote(tickets.filter(_.id == lift(ticket.id)).update(lift(ticket)))

    run(query)
  }

  def delete(id: Long): Future[Long] = {
    val query = quote(tickets.filter(_.id == lift(id)).delete)

    run(query)
  }

}
