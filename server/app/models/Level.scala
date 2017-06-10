package models

import java.util.Date
import javax.inject.{Inject, Provider}

import play.api.{Application, Play}
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.Future

/**
  * Created by sebastien on 06.06.17.
  */
case class Level(id: Int, name: String)

class LevelTableDef(tag: Tag) extends Table[Level](tag, "level") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  override def * : ProvenShape[Level] =
    (id, name) <>(Level.tupled, Level.unapply)
}

object Levels {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val levels = TableQuery[LevelTableDef]

  def findAll: Future[Seq[Level]] = {
    dbConfig.db.run(levels.result)
  }

}