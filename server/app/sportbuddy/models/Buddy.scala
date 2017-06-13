package sportbuddy.models


import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.jdbc.GetResult

import scala.concurrent.Future

case class Buddy (id: Int, description: String, personId: Int, activityId: Int, locationId: Int, levelId: Int)

class BuddyTableDef (tag: Tag) extends Table[Buddy](tag, "buddy") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def description = column[String]("description")
  def personId = column[Int]("person_id")
  def activityId = column[Int]("activity_id")
  def locationId = column[Int]("location_id")
  def levelId = column[Int]("level_id")

  override def * = (id, description, personId, activityId, locationId, levelId) <> (Buddy.tupled, Buddy.unapply)

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

    implicit val getBuddyAPIResult = GetResult(r => BuddyAPI(r.nextInt, r.nextString, r.nextString,
      r.nextString, r.nextString, r.nextString(), r.nextString(), r.nextString(), r.nextString()))

  def getBuddies(activity: String, level: String, city: String): Future[Seq[BuddyAPI]] =  {

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

    // Good working "old school" way
    val query = sql"""SELECT person.id, firstname, lastname, buddy.description, email, birthdate, activity.name, level.name, location.city FROM buddy
                        INNER JOIN person ON buddy.person_id = person.id
                        INNER JOIN location ON buddy.location_id = location.id
                        INNER JOIN level ON buddy.level_id = level.id
                        INNER JOIN activity ON buddy.activity_id = activity.id""".as[BuddyAPI]

    dbConfig.db.run(query)
  }

  def listAll: Future[Seq[Buddy]] = {
    dbConfig.db.run(buddies.result)
  }

  def add(buddy: Buddy): Future[Int] = {
    dbConfig.db.run((buddies returning buddies.map(_.id)) += buddy)
  }

}