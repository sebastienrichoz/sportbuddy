package services

import controller.ServiceDef
import models._
import play.api.libs.json.Json

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.stm.Ref

object Service extends ServiceDef {

  private val value = Ref(0)

  def get(): Int = value.single.get

  def increment(step: Int): Int = value.single.transformAndGet(_ + step)

  def reset(): Int = value.single.transformAndGet(_ => 0)

  def addBuddy(personId: Int, activityId: Int, levelId: Int, locationId: Int): Int = {
    val f = Buddies.add(Buddy(personId, activityId, levelId, locationId))
    Await.result(f, 5 seconds)
  }

  def getActivities(): String = {
    val f = Activities.findAll
    Await.result(f, 5 seconds)
    "activities"
  }

  def getLevels(): String = {
    val f = Levels.findAll
    Await.result(f, 5 seconds)
    "levels"
  }

  def getLocations(): String = {
    val f = Locations.findAll
    Await.result(f, 5 seconds)
    "locations"
  }

  def getBuddies(activity: String, level: String, location: String): String = {
    val buddiesDTO = Buddies.getBuddies(activity, level, location)
    //val json = Json.toJson(buddiesDTO)
    ""
  }
}