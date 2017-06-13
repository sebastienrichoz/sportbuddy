package sportbuddy.models

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.Future

case class Level(id: Int, name: String)

class LevelTableDef(tag: Tag) extends Table[Level](tag, "level") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  override def * : ProvenShape[Level] = (id, name) <>(Level.tupled, Level.unapply)
}

object Levels {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val levels = TableQuery[LevelTableDef]

  def findAll: Future[Seq[Level]] = {
    dbConfig.db.run(levels.result)
  }

  def findByName(name: String): Future[Seq[Level]] = {
    val query = for {
      a <- levels if a.name like name
    } yield a
    dbConfig.db.run(query.result)
  }

}
