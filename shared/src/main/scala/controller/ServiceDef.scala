package controller


trait ServiceDef {

  def get(): Int

  def increment(step: Int): Int

  def reset(): Int

  def addBuddy(personId: Int, activityId: Int, levelId: Int, locationId: Int): Int

  def getActivities(): String

  def getLevels(): String

  def getLocations(): String

  //def getBuddies(activityId: Int = null, levelId: Int = null, locationId: Int = null): String
  def getBuddies(activity: String, level: String, city: String): String
}
