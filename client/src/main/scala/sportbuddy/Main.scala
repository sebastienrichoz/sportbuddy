package sportbuddy

import autowire._
import boopickle.Default._
import mhtml.mount
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.html.{Input, Select}

import scala.scalajs.js.{Date, JSApp}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}
import scala.xml.Group

object Main extends JSApp {

  def getAge(date: String): Int = {
    var today = new Date()
    var birthDate = new Date(date)
    var age = today.getFullYear() - birthDate.getFullYear()
    var m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m == 0 && today.getDate() < birthDate.getDate())) {
      age -= 1
    }
    age
  }

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

          val content = {
            Group(buddies.map(b =>
              <div class="comment-body">
                <div class="user-img"> <img src="assets/images/avatar.png" alt="user" class="img-circle" /></div>
                <div class="mail-contnet">
                  <h5>{ b.firstname } { b.lastname }, { getAge(b.birthdate) }</h5><span class="time"><b>{ b.activity }</b> { b.level }  -  <i>{ b.location }</i></span>
                  <br/><span class="mail-desc">{ b.description }</span>
                  <span>{ b.email } <a href={ "mailto:" + b.email } class="btn btn btn-rounded btn-default btn-outline m-r-5"><i class="ti-check text-success m-r-5"></i>Contact</a></span>
                </div>
              </div>
            ))
          }

          dom.document.getElementById("sportbuddies").innerHTML = "" // Remove previous buddies
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
            searchBuddies()
          }

          <select id="activitySelector" onchange={ onActivityChanged _ } class="profile-pic form-control"></select>
        }

        mount(dom.document.getElementById("activities"), activitySelector)

        val activities = {
          Group(<option value={""}>All activities</option> +: activitiesReturned.map(elem => <option value={ elem.name }>{ elem.name }</option>))
        }

        mount(dom.document.getElementById("activitySelector"), activities)
        mount(dom.document.getElementById("addBuddyActivitySelector"), activities)

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
            searchBuddies()
          }

          <select id="levelSelector" onchange={ onLevelChanged _ } class="profile-pic form-control"></select>
        }

        mount(dom.document.getElementById("levels"), levelSelector)

        val levels = {
          Group(<option value={""}>All levels</option> +: levelsReturned.map(l =>  <option value={ l.name }>{ l.name }</option>))
        }

        mount(dom.document.getElementById("levelSelector"), levels)
        mount(dom.document.getElementById("addBuddyLevelSelector"), levels)

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
            searchBuddies()
          }

          <select id="citySelector" onchange={ onCityChanged _ } class="profile-pic form-control"></select>
        }

        mount(dom.document.getElementById("cities"), citySelector)

        val cities = {
          Group(<option value={""}>All cities</option> +: citiesReturned.map(c => <option value={ c.city }>{ c.city }</option>))
        }

        mount(dom.document.getElementById("citySelector"), cities)
        mount(dom.document.getElementById("addBuddyCitySelector"), cities)

      case Failure(reason) =>
        println("Unable to retrieve the cities")
        reason.printStackTrace()
    }


    /**
      * Search for all buddies, without filter
      */
    searchBuddies()


    /**
      * Add buddy
      */
    val addBuddyButton = {

      def onClick(event: Event) = {

        val firstname = dom.document.getElementById("firstname").asInstanceOf[Input].value
        val lastname = dom.document.getElementById("lastname").asInstanceOf[Input].value
        val description = dom.document.getElementById("description").asInstanceOf[Input].value
        val email = dom.document.getElementById("email").asInstanceOf[Input].value
        val birthdate = dom.document.getElementById("birthdate").asInstanceOf[Input].value
        val bdescription = dom.document.getElementById("bdescription").asInstanceOf[Input].value
        val activity = dom.document.getElementById("addBuddyActivitySelector").asInstanceOf[Select].value
        val level = dom.document.getElementById("addBuddyLevelSelector").asInstanceOf[Select].value
        val city = dom.document.getElementById("addBuddyCitySelector").asInstanceOf[Select].value

        println("addbuddy")
        println(firstname)
        println(lastname)
        println(description)
        println(email)
        println(birthdate)
        println(bdescription)
        println(activity)
        println(level)
        println(city)

        if (!firstname.isEmpty && !lastname.isEmpty && !description.isEmpty && !email.isEmpty && !birthdate.isEmpty && !bdescription.isEmpty&& !activity.isEmpty && !level.isEmpty && !city.isEmpty) {
          client.addBuddy(firstname, lastname, description, email, birthdate, bdescription, activity, level, city).call().onComplete {
            case Success(result) =>
              dom.window.alert("New buddy !")
              searchBuddies()

            case Failure(reason) =>
              dom.window.alert("Unexpected error :(")
              reason.printStackTrace()
          }
        }
        else {
          dom.window.alert("All fields are required")
        }
      }

      <button onclick={ onClick _} type="button" class="btn btn-success">Post</button>
    }
    mount(dom.document.getElementById("addBuddySubmit"), addBuddyButton)
  }
}
