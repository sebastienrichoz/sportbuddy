package services

import controller.ServiceDef
import models._
import sharedmodels.{Activity, BuddyDTO, Level, Location}

import scala.concurrent.Await
import scala.concurrent.duration._

object Service extends ServiceDef {

  def getActivities(): Seq[Activity] = {
    val f = Activities.findAll
    val elems = Await.result(f, 5 seconds)
    elems.sortBy(e => e.name)
  }

  def getLevels(): Seq[Level] = {
    val f = Levels.findAll
    Await.result(f, 5 seconds)
  }

  def getLocations(): Seq[Location] = {
    val f = Locations.findAll
    val elems = Await.result(f, 5 seconds)
    elems.sortBy(e => e.city)
  }

  def getBuddies(activity: String, level: String, location: String): Seq[BuddyDTO] = {
    val f = Buddies.getBuddies(activity, level, location)
    Await.result(f, 5 seconds)
  }
}