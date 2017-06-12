package controller

import java.nio.ByteBuffer
import java.util.Date

import boopickle.Default._
import models._
//import models.Formatters._
import play.api.libs.functional.syntax._
import play.twirl.api.{Html, HtmlFormat}
import services.Service

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import play.api.libs.json._
import sharedmodels.{Activity, BuddyDTO, Level, Location}

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

  implicit val buddyDTOWrites: Writes[BuddyDTO] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "firstname").write[String] and
      (JsPath \ "lastname").write[String] and
      (JsPath \ "description").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "birthdate").write[String] and
      (JsPath \ "activity").write[String] and
      (JsPath \ "level").write[String] and
      (JsPath \ "city").write[String]
    )(unlift(BuddyDTO.unapply))

  val index = Action { request =>
    Ok(views.html.index(Service.brand()))
  }

  val greet = Action { request =>
    val clientScript = {
      Seq("client-opt.js", "client-fastopt.js")
        .find(name => getClass.getResource(s"/public/$name") != null)
        .map(name => controllers.routes.Assets.at(name).url)
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

  val getActivities = Action { request =>
    Ok(Json.toJson(Service.getActivities()))
  }

  val getLevels = Action { request =>
    Ok(Json.toJson(Service.getLevels()))
  }

  val getLocations = Action { request =>
    Ok(Json.toJson(Service.getLocations()))
  }

  private def exist(s: String) = s != ""

  def getBuddies(activity: String, level: String, city: String): Action[AnyContent] = Action.async { request =>

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
