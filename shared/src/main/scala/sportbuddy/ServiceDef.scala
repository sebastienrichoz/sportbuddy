package sportbuddy

import models._

trait ServiceDef {

  def brand() = "SportBuddy"

  def getActivities(): Seq[ActivityAPI]

  def getLevels(): Seq[LevelAPI]

  def getLocations(): Seq[LocationAPI]

  def getBuddies(activity: String, level: String, city: String): Seq[BuddyAPI]

}
