package sportbuddy.services

import sportbuddy.ServiceDef
import sportbuddy.models._

import scala.concurrent.Await
import scala.concurrent.duration._

object Service extends ServiceDef {

  def getActivities(): Seq[ActivityAPI] = {
    val f = Activities.findAll
    val elems = Await.result(f, 5 seconds)
    elems.sortBy(e => e.name).map(a => ActivityAPI(a.id, a.name))
  }

  def getLevels(): Seq[LevelAPI] = {
    val f = Levels.findAll
    val elems = Await.result(f, 5 seconds)
    elems.map(l => LevelAPI(l.id, l.name))
  }

  def getLocations(): Seq[LocationAPI] = {
    val f = Locations.findAll
    val elems = Await.result(f, 5 seconds)
    elems.sortBy(l => l.name).map(l => LocationAPI(l.id, l.name))
  }

  def getBuddies(activity: String, level: String, location: String): Seq[BuddyAPI] = {
    def exist(s: String) = s != ""

    val f = Buddies.getBuddies(activity, level, location)
    val buddies = Await.result(f, 5 seconds)

    // Filter specific buddies depending the activity, level, or city selected
    val filteredBuddies = (activity, level, location) match {
      case (a, l, c) if !exist(a) && !exist(l) && !exist(c) => buddies
      case (a, l, c) if !exist(a) && !exist(l) &&  exist(c) => buddies.filter(x => x.location == location)
      case (a, l, c) if !exist(a) &&  exist(l) && !exist(c) => buddies.filter(x => x.level == level)
      case (a, l, c) if !exist(a) &&  exist(l) &&  exist(c) => buddies.filter(x => x.location == location && x.level == level)
      case (a, l, c) if  exist(a) && !exist(l) && !exist(c) => buddies.filter(x => x.activity == activity)
      case (a, l, c) if  exist(a) && !exist(l) &&  exist(c) => buddies.filter(x => x.activity == activity && x.location == location)
      case (a, l, c) if  exist(a) &&  exist(l) && !exist(c) => buddies.filter(x => x.activity == activity && x.level == level)
      case (a, l, c) if  exist(a) &&  exist(l) &&  exist(c) => buddies.filter(x => x.activity == activity && x.level == level && x.location == location)
    }

    filteredBuddies
  }

}
