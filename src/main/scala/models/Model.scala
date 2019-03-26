package models

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._

case class Message(message: String)

case class Ticket(id: Long, title: String)

case class TicketData(title: String) {
  require(title.length < 256, "OMG")
  require(title.nonEmpty, "title cannot be empty")

  def getTicket(id: Long = -1L) = Ticket(id, title)
}

case class TicketFilter(startId: Option[Long], length: Int) {
  require(length < 101, "length can not be bigger than 100")
  require(length > 1, "length can not be smaller than 1")
}

case class TicketResult(nextPageStartId: Option[Long], tickets: List[Ticket])

object TicketFilter {
  def get: Directive1[TicketFilter] = parameters('startId.as[Long]?, 'length.as[Int]).as(TicketFilter.apply)
}
