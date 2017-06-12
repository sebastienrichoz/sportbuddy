package controller

import sharedmodels.{Activity, BuddyDTO, Level, Location}

trait ServiceDef {

  def brand() = "SportBuddy"

  def getActivities(): Seq[Activity]

  def getLevels(): Seq[Level]

  def getLocations(): Seq[Location]

  def getBuddies(activity: String, level: String, city: String): Seq[BuddyDTO]
}
