package models

import java.util.Date

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json._
import play.api.libs.functional.syntax._
import sharedmodels.BuddyDTO
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.jdbc.GetResult

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}


case class Buddy(personId: Int, activityId: Int, levelId: Int, locationId: Int)

class BuddyTableDef (tag: Tag) extends Table[Buddy](tag, "buddy") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def personId = column[Int]("person_id")
  def activityId = column[Int]("activity_id")
  def locationId = column[Int]("location_id")
  def levelId = column[Int]("level_id")

  override def * = (personId, activityId, locationId, levelId) <> (Buddy.tupled, Buddy.unapply)

  def buddyFk = foreignKey("fk_person", personId, TableQuery[PersonTableDef])(p =>
    p.id/*, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade*/)
  def activityFk = foreignKey("fk_activity", activityId, TableQuery[ActivityTableDef])(a =>
    a.id/*, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade*/)
  def locationFk = foreignKey("fk_location", locationId, TableQuery[LocationTableDef])(l =>
    l.id/*,  onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade*/)
  def levelFk = foreignKey("fk_level", levelId, TableQuery[LevelTableDef])(l =>
    l.id/*, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade*/)
}

object Buddies {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val buddies = TableQuery[BuddyTableDef]


  implicit val getBuddyDTOResult = GetResult(r => BuddyDTO(r.nextInt, r.nextString, r.nextString,
    r.nextString, r.nextString, r.nextString(), r.nextString, r.nextString, r.nextString))

  def getBuddies(activity: String, level: String, city: String): Future[Seq[BuddyDTO]] =  {

    val people = TableQuery[PersonTableDef]
    val activities = TableQuery[ActivityTableDef]
    val levels = TableQuery[LevelTableDef]
    val locations = TableQuery[LocationTableDef]

    // TODO : attempts to write sql query without sql
//    val query = for {
//      ((((b, p), a), lvl), loc) <- buddies join people on (_.personId === _.id) join activities on (_._1.activityId === _.id) join levels on (_._1._1.levelId === _.id) join locations on (_._1._1._1.locationId === _.id)
//    } yield (p.id, p.firstname, p.lastname, p.description, p.email, p.birthdate, a.name, lvl.name, loc.city)

//    val query = for {
//      b <- buddies
//      p <- people if b.personId === p.id
//      a <- activities if b.activityId === a.id
//      lvl <- levels if b.levelId === lvl.id
//      loc <- locations if b.locationId === loc.id
//    } yield (p.id, p.firstname, p.lastname, p.description, p.email, p.birthdate, a.name, lvl.name, loc.city)

    // Good "old school" way is working compared to the attempts
    val query = sql"""SELECT person.id, firstname, lastname, description, email, birthdate, activity.name, level.name, location.city FROM buddy
                        INNER JOIN person ON buddy.person_id = person.id
                        INNER JOIN location ON buddy.location_id = location.id
                        INNER JOIN level ON buddy.level_id = level.id
                        INNER JOIN activity ON buddy.activity_id = activity.id""".as[BuddyDTO]

    val f = dbConfig.db.run(query)
    var fullbuddies : List[BuddyDTO] = List()

    Await.ready(f, 5 seconds)

//    f andThen {
//      case Success(b) => b :: fullbuddies
//    } andThen {
//      case _ => fullbuddies
//    }
  }

  def listAll: Future[Seq[Buddy]] = {
    dbConfig.db.run(buddies.result)
  }

  def add(buddy: Buddy): Future[Int] = {
    dbConfig.db.run((buddies returning buddies.map(_.id)) += buddy)
  }

}
//
//object BuddyDTO {
//  def apply(nextInt: Int, nextString: String, nextString1: String, nextString2: String, nextString3: String, date: Date, nextString4: String, nextString5: String, nextString6: String): BuddyDTO = {
//    new BuddyDTO(nextInt, nextString, nextString1, nextString2, nextString3, date, nextString4, nextString5, nextString6)
//  }
//}
//
//object Formatters {
//  implicit val BuddyDTOFormat: OFormat[BuddyDTO] = Json.format[BuddyDTO]
//}