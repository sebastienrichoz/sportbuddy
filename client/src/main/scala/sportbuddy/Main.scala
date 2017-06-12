package sportbuddy

import autowire._
import boopickle.Default._
import mhtml.mount
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.html.Select

import scala.scalajs.js.JSApp
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}
import scala.xml.Group

object Main extends JSApp {

  def main(): Unit = {

    val client = Client[ServiceDef]

    var activitySelectedValue: String = ""
    var levelSelectedValue: String = ""
    var citySelectedValue: String = ""

    /**
      * Perform request and display buddies
      */
    def searchBuddies(): Unit = {

      println("search buddies")
      println(activitySelectedValue)
      println(levelSelectedValue)
      println(citySelectedValue)

      client.getBuddies(activitySelectedValue, levelSelectedValue, citySelectedValue).call().onComplete {
        case Success(buddies) =>

          // TODO : remove previous buddies

          val content = {
            Group(buddies.map(b =>
              <div class="comment-body">
                <div class="user-img"> <img src="assets/images/avatar.png" alt="user" class="img-circle" /></div>
                <div class="mail-contnet">
                  <h5>{ b.firstname } { b.lastname }, { b.birthdate.toString }</h5><span class="time"><b>{ b.activity }</b> { b.level }  -  <i>{ b.location }</i></span>
                  <br/><span class="mail-desc">{ b.description }</span>
                  <span>{ b.email } <a href={ "mailto:" + b.email } class="btn btn btn-rounded btn-default btn-outline m-r-5"><i class="ti-check text-success m-r-5"></i>Contact</a></span>
                </div>
              </div>
            ))
          }

          mount(dom.document.getElementById("sportbuddies"), content)

        case Failure(reason) =>
          println("Unable to retrieve the buddies")
          reason.printStackTrace()
      }
    }


    /**
      * Load activities
      *
      * Add onchange listener to update local variable.
      */
    client.getActivities().call().onComplete {
      case Success(activitiesReturned) =>

        val activitySelector = {

          def onActivityChanged(event: Event) = {
            println("activity changed")
            activitySelectedValue = event.target.asInstanceOf[Select].value
            println("selected : " + activitySelectedValue)
          }

          <select id="activitySelector" onchange={ onActivityChanged _ } class="profile-pic form-control"></select>
        }

        mount(dom.document.getElementById("activities"), activitySelector)

        val activities = {
          Group(<option>All activities</option> +: activitiesReturned.map(elem => <option value={ elem.name }>{ elem.name }</option>))
        }

        mount(dom.document.getElementById("activitySelector"), activities)

      case Failure(reason) =>
        println("Unable to retrieve the activities")
        reason.printStackTrace()
    }


    /**
      * Load levels
      *
      * Add onchange listener to update local variable.
      */
    client.getLevels().call().onComplete {
      case Success(levelsReturned) =>

        val levelSelector = {

          def onLevelChanged(event: Event) = {
            println("level changed")
            levelSelectedValue = event.target.asInstanceOf[Select].value
            println("selected : " + levelSelectedValue)
          }

          <select id="levelSelector" onchange={ onLevelChanged _ } class="profile-pic form-control"></select>
        }

        mount(dom.document.getElementById("levels"), levelSelector)

        val levels = {
          Group(<option>All levels</option> +: levelsReturned.map(l =>  <option value={ l.name }>{ l.name }</option>))
        }

        mount(dom.document.getElementById("levelSelector"), levels)

      case Failure(reason) =>
        println("Unable to retrieve the levels")
        reason.printStackTrace()
    }


    /**
      * Load locations
      *
      * Add onchange listener to update local variable.
      */
    client.getLocations().call().onComplete {
      case Success(citiesReturned) =>

        val citySelector = {

          def onCityChanged(event: Event) = {
            println("city changed")
            citySelectedValue = event.target.asInstanceOf[Select].value
            println("selected : " + citySelectedValue)
          }

          <select id="citySelector" onchange={ onCityChanged _ } class="profile-pic form-control"></select>
        }

        mount(dom.document.getElementById("cities"), citySelector)

        val cities = {
          Group(<option value={""}>All cities</option> +: citiesReturned.map(c => <option value={ c.city }>{ c.city }</option>))
        }

        mount(dom.document.getElementById("citySelector"), cities)

      case Failure(reason) =>
        println("Unable to retrieve the cities")
        reason.printStackTrace()
    }


    /**
      * Define search button
      */
    val searchButton = {

      def onClick(event: Event) = {
        searchBuddies()
      }

      <button onclick={ onClick _} type="button" class="btn btn-success btn-outline" style="margin-top:15px;height:30px">
        <i class="fa fa-search"></i>
      </button>
    }
    mount(dom.document.getElementById("searchbuddies"), searchButton)


    /**
      * Search for all buddies, without filter
      */
    searchBuddies()
  }
}
