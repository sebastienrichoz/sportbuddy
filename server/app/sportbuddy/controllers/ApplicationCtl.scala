package sportbuddy.controllers

import java.nio.ByteBuffer

import boopickle.Default._
import sportbuddy.services.Service

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import sportbuddy.{Server, ServiceDef}

/**
  * Implement routes defined in conf/routes
  */
class ApplicationCtl extends Controller {

  /** Index page **/
  val index = Action { request =>
    Ok(sportbuddy.views.html.index(Service.brand()))
  }

  def service(path: String): Action[RawBuffer] = Action.async(parse.raw) { request =>
    // get the request body as ByteString
    val b = request.body.asBytes(parse.UNLIMITED).get

    // call Autowire route
    Server.route[ServiceDef](Service)(
      autowire.Core.Request(path.split("/"),
        Unpickle[Map[String, ByteBuffer]].fromBytes(b.asByteBuffer))
    ).map { buffer =>
      val data = Array.ofDim[Byte](buffer.remaining())
      buffer.get(data)
      Ok(data)
    }
  }

}
