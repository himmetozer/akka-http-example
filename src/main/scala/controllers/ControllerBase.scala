package controllers

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpResponse, ResponseEntity, StatusCodes}
import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives.{complete, mapInnerRoute}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.syntax._
import io.circe.{Encoder, Json}
import models.JsonProtocol.Implicits.messageEncoder
import models.Message
import nl.grons.metrics4.scala.{DefaultInstrumented, Timer}

import scala.concurrent.ExecutionContext.Implicits.global

trait ControllerBase extends DefaultInstrumented {

  val Ok = (data: Json) =>
    complete(Marshal(data).to[ResponseEntity].map { e =>
      HttpResponse(entity = e)
    })

  val Created = (data: Json) =>
    complete(Marshal(data).to[ResponseEntity].map { e =>
      HttpResponse(entity = e, status = StatusCodes.Created)
    })

  val NotFound = complete(HttpResponse(status = StatusCodes.NotFound))

  val InternalServerError = (e: Throwable) =>
    complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e =>
      HttpResponse(entity = e, status = StatusCodes.InternalServerError)
    })

  def withTimer(timer: Timer): Directive0 =
    mapInnerRoute(inner => ctx => timer.timeFuture(inner(ctx)))

  implicit def toJson[T: Encoder](m: T): Json = m.asJson

}
