package sportbuddy.models

import play.api.{Application, Play}
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.functional.syntax._
import play.api.libs.json._
import sportbuddy.models.Activity
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.Future

case class Activity(id: Int, name: String)

class ActivityTableDef(tag: Tag) extends Table[Activity](tag, "activity") {

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("name")

  override def * : ProvenShape[Activity] = (id, name) <> (Activity.tupled, Activity.unapply)
}

object Activities {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val activities = TableQuery[ActivityTableDef]

  def findAll: Future[Seq[Activity]] = {
    dbConfig.db.run(activities.result)
  }

}