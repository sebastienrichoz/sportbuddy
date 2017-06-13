package sportbuddy.models

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.Future

case class Activity(id: Int, name: String)

class ActivityTableDef(tag: Tag) extends Table[Activity](tag, "activity") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  override def * : ProvenShape[Activity] = (id, name) <> (Activity.tupled, Activity.unapply)
}

object Activities {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val activities = TableQuery[ActivityTableDef]

  def findAll: Future[Seq[Activity]] = {
    dbConfig.db.run(activities.result)
  }

  def findByName(name: String): Future[Seq[Activity]] = {
    val query = for {
      a <- activities if a.name like name
    } yield a
    dbConfig.db.run(query.result)
  }

}
