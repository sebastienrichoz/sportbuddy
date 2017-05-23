package counter

import java.nio.ByteBuffer

import boopickle.Default._

import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.twirl.api.{Html, HtmlFormat}

class CounterCtl extends Controller {

  val greet = Action { request =>
    Ok("Hello, world!")
  }

  val index = Action { request =>
    val clientScript = {
      Seq("client-opt.js", "client-fastopt.js")
        .find(name => getClass.getResource(s"/public/$name") != null)
        .map(name => controllers.routes.Assets.versioned(name).url)
        .get
    }
    val html =
      HtmlFormat.fill(List(
        Html("<html><head><script src=\""),
        HtmlFormat.escape(clientScript),
        Html("\"></script></head><body></body></html>")
      ))
    Ok(html)
  }

  val increment = Action(parse.json) { request =>
    request.body.validate[Int]
      .fold(
        _ => BadRequest,
        step => Ok(Json.toJson(Service.increment(step)))
      )
  }

  def service(path: String) = Action.async(parse.raw) { request =>
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
