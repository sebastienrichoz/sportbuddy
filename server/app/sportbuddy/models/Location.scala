package sportbuddy.models

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

case class Location(id: Int, city: String)

class LocationTableDef(tag: Tag) extends Table[Location](tag, "location") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def city = column[String]("city")

  override def * = (id, city) <> (Location.tupled, Location.unapply)
}

object Locations {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val locations = TableQuery[LocationTableDef]

  def findAll: Future[Seq[Location]] = {
    dbConfig.db.run(locations.result)
  }

  def findByName(name: String): Future[Seq[Location]] = {
    val query = for {
      a <- locations if a.city like name
    } yield a
    dbConfig.db.run(query.result)
  }

}
