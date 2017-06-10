package models


import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
  * Created by sebastien on 06.06.17.
  */
case class Location(id: Int, city: String)

class LocationTableDef(tag: Tag) extends Table[Location](tag, "location") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def city = column[String]("city")

  override def * =
    (id, city) <> (Location.tupled, Location.unapply)
}

object Locations {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val locations = TableQuery[LocationTableDef]

  def findAll: Future[Seq[Location]] = {
    dbConfig.db.run(locations.result)
  }

}
