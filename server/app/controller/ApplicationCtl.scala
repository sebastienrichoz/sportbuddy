package controller

import java.nio.ByteBuffer

import boopickle.Default._
import models._
import models.Formatters._
import play.api.libs.functional.syntax._
import play.twirl.api.{Html, HtmlFormat}
import services.Service

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import play.api.libs.json._

/**
  * Implement routes defined in conf/routes
  */
class ApplicationCtl extends Controller {

  // Declaration of implicit Json Writers
  implicit val LocationWrites: Writes[Location] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    )(unlift(Location.unapply))

  implicit val ActivityWrites: Writes[Activity] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    )(unlift(Activity.unapply))

  implicit val LevelWrites: Writes[Level] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    )(unlift(Level.unapply))

  val greet = Action { request =>
    Ok(views.html.index("SportBuddy"))
  }

  def index = Action {
    Ok(views.html.index("SportBuddy"))
  }

//  val index = Action { request =>
//    val clientScript = {
//      Seq("client-opt.js", "client-fastopt.js")
//        .find(name => getClass.getResource(s"/public/$name") != null)
//        .map(name => controllers.routes.Assets.versioned(name).url)
//        .get
//    }
//    val html =
//      HtmlFormat.fill(List(
//        Html("<html><head><script src=\""),
//        HtmlFormat.escape(clientScript),
//        Html("\"></script></head><body></body></html>")
//      ))
//    Ok(html)
//  }

  val getActivities = Action.async { request =>
    val activities = Activities.findAll
    activities.map(a => Ok(Json.toJson(a)))
  }

  val getLevels = Action.async { request =>
    val levels = Levels.findAll
    levels.map(l => Ok(Json.toJson(l)))
  }

  val getLocations = Action.async { request =>
    val locations = Locations.findAll
    locations.map(l => Ok(Json.toJson(l)))
  }

  private def exist(s: String) = s != ""

  def getBuddies(activity: String, level: String, city: String) = Action.async { request =>

    val buddiesDTO = Buddies.getBuddies(activity, level, city)

    // Filter specific buddies depending the activity, level, or city selected
    val filteredBuddies = (activity, level, city) match {
      case (a, l, c) if !exist(a) && !exist(l) && !exist(c) => buddiesDTO
      case (a, l, c) if !exist(a) && !exist(l) &&  exist(c) => buddiesDTO.map(b => b.filter(x => x.city == city))
      case (a, l, c) if !exist(a) &&  exist(l) && !exist(c) => buddiesDTO.map(b => b.filter(x => x.level == level))
      case (a, l, c) if !exist(a) &&  exist(l) &&  exist(c) => buddiesDTO.map(b => b.filter(x => x.city == city && x.level == level))
      case (a, l, c) if  exist(a) && !exist(l) && !exist(c) => buddiesDTO.map(b => b.filter(x => x.activity == activity))
      case (a, l, c) if  exist(a) && !exist(l) &&  exist(c) => buddiesDTO.map(b => b.filter(x => x.activity == activity && x.city == city))
      case (a, l, c) if  exist(a) &&  exist(l) && !exist(c) => buddiesDTO.map(b => b.filter(x => x.activity == activity && x.level == level))
      case (a, l, c) if  exist(a) &&  exist(l) &&  exist(c) => buddiesDTO.map(b => b.filter(x => x.activity == activity && x.level == level && x.city == city))
    }

    filteredBuddies.map(l => Ok(Json.toJson(l)))
  }

  val increment = Action(parse.json) { request =>
    request.body.validate[Int]
      .fold(
        _ => BadRequest,
        step => Ok(Json.toJson(Service.addBuddy(step, step, step, step)))
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
