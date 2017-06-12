package sportbuddy.models

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

case class Person(id: Int, firstname: String, lastname: String, description: String, email: String, birthdate: String)

class PersonTableDef(tag: Tag) extends Table[Person](tag, "person") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def firstname = column[String]("firstname")
  def lastname = column[String]("lastname")
  def description = column[String]("description")
  def email = column[String]("email")
  def birthdate = column[String]("birthdate")

  override def * =
    (id, firstname, lastname, description, email, birthdate) <>(Person.tupled, Person.unapply)
}

object People {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val people = TableQuery[PersonTableDef]

  def findAll: Future[Seq[Person]] = {
    dbConfig.db.run(people.result)
  }

}
