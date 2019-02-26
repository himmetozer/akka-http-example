package models

import io.circe._
import io.circe.generic.semiauto._

object JsonProtocol {

  object Implicits {

    implicit val ticketDataDecoder: Decoder[TicketData] = deriveDecoder

    implicit val ticketEncoder: Encoder[Ticket] = deriveEncoder
    implicit val ticketResultEncoder: Encoder[TicketResult] = deriveEncoder
    implicit val messageEncoder: Encoder[Message] = deriveEncoder
    //import io.circe.syntax._
    //implicit val encoder: Encoder[Ticket] = m => Json.obj("id" -> m.id.asJson, "title" -> m.title.asJson)

  }

}
