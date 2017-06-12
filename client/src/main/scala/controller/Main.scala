package controller

import java.util.Date

import autowire._
import boopickle.Default._
import mhtml.{Var, mount}
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.html.{Input, Select}
import sharedmodels.BuddyDTO

import scala.scalajs.js.{JSApp, JSON}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}
import scala.xml.Group

object Main extends JSApp {

  def main(): Unit = {

    val client = Client[ServiceDef]
    var activityValue: String = ""
    var levelValue: String = ""
    var cityValue: String = ""

    client.getActivities().call().onComplete {
      case Success(activitiesReturned) =>

        val activitySelect = {
          def onActivityChanged(event: Event) = {
            println("activity changed")
            activityValue = event.target.asInstanceOf[Select].value
          }

          <select id="activitySelector" onchange={ onActivityChanged _ } class="profile-pic form-control">
          </select>
        }

        mount(dom.document.getElementById("activities"), activitySelect)

        val activities = {
          Group(<option>All activities</option> +: activitiesReturned.map(elem => <option value={ elem.name }>{ elem.name }</option>))
        }

        mount(dom.document.getElementById("activitySelector"), activities)

      case Failure(reason) =>
        println("Unable to retrieve the activities")
        reason.printStackTrace()
    }

    client.getLevels().call().onComplete {
      case Success(levelsReturned) =>

        val levelSelect = {
          def onLevelChanged(event: Event) = {
            println("level changed")
            levelValue = event.target.asInstanceOf[Select].value
          }

          <select id="levelSelector" onchange={ onLevelChanged _ } class="profile-pic form-control">
          </select>
        }

        mount(dom.document.getElementById("levels"), levelSelect)

        val levels = {
          Group(<option>All levels</option> +: levelsReturned.map(l =>
            <option value={ l.name }>{ l.name }</option>
          ))
        }

        mount(dom.document.getElementById("levelSelector"), levels)

      case Failure(reason) =>
        println("Unable to retrieve the levels")
        reason.printStackTrace()
    }

    client.getLocations().call().onComplete {
      case Success(citiesReturned) =>

        val citySelect = {
          def onCityChanged(event: Event) = {
            println("city changed")
            cityValue = event.target.asInstanceOf[Select].value
          }

          <select id="citySelector" onchange={ onCityChanged _ } class="profile-pic form-control">
          </select>
        }

        mount(dom.document.getElementById("cities"), citySelect)

        val cities = {
          Group(<option value={""}>All cities</option> +: citiesReturned.map(c =>
            <option value={ c.city }>{ c.city }</option>
          ))
        }

        mount(dom.document.getElementById("citySelector"), cities)

      case Failure(reason) =>
        println("Unable to retrieve the cities")
        reason.printStackTrace()
    }

//    client.get().call().onComplete {
//      case Success(currentValue) =>
//
//        val app = {
//
//          val counter = Var(currentValue)
//          var step = 1
//
//          def onIncrement(): Unit = {
//            dom.ext.Ajax.post(
//              url = "/inc",
//              data = step.toString,
//              headers = Map("Content-Type" -> "application/json")
//            ).foreach { xhr =>
//              if (xhr.status == 200) {
//                val x = JSON.parse(xhr.responseText).asInstanceOf[Int]
//                counter := x
//              }
//            }
////            client.increment(step).call().foreach(x => counter := x)
//          }
//
//          def onReset(): Unit = {
//            client.reset().call().foreach(x => counter := x)
//          }
//
//          def onChangeStep(event: Event): Unit = {
//            step = event.target.asInstanceOf[Input].value.toInt
//          }
//
//          <div style="text-align: center">
//            <h1 id="counter">{ counter }</h1>
//            <h1><input type="number" value={ step.toString } onchange={ onChangeStep _ } /></h1>
//            <div>
//              <button id="inc" onclick={ onIncrement _ }>Increment</button>
//              <button id="reset" onclick={ onReset _ }>Reset</button>
//            </div>
//          </div>
//        }
//
//        mount(dom.document.body, app)
//
//
//      case Failure(reason) =>
//        println("Unable to retrieve the buddies")
//        reason.printStackTrace()
//    }


    client.getBuddies("", "", "").call().onComplete {
      case Success(values) =>

        var buddies = values

        val buddy = {
          Group(buddies.map(b =>
            <div class="comment-body">
              <div class="user-img"> <img src="assets/images/avatar.png" alt="user" class="img-circle" /></div>
              <div class="mail-contnet">
                <h5>{ b.firstname } { b.lastname }, { b.birthdate.toString }</h5><span class="time"><b>{ b.activity }</b> { b.level }  -  <i>{ b.city }</i></span>
                <br/><span class="mail-desc">{ b.description }</span>
                <span>{ b.email } <a href={ "mailto:" + b.email } class="btn btn btn-rounded btn-default btn-outline m-r-5"><i class="ti-check text-success m-r-5"></i>Contact</a></span>
              </div>
            </div>
          ))
        }

        val search = {

          def onClick() = {
            println(activityValue)
            println(levelValue)
            println(cityValue)


            // Get buddies from filters
            dom.ext.Ajax.get(
              url = "/buddies?activity=" + activityValue + "&level=" + levelValue + "&city=" + cityValue,
              headers = Map("Content-Type" -> "application/json")
            ).foreach { xhr =>
              if (xhr.status == 200) {

                println(xhr.responseText)

                val res = JSON.parse(xhr.responseText)
                println(res)

                // TODO deserialize json to Seq[BuddyDTO]
                // buddies = res
              }
            }
          }

          <button onclick={ onClick _ } type="button" class="btn btn-success btn-outline" style="margin-top:15px;height:30px"><i class="fa fa-search"></i></button>
        }

        mount(dom.document.getElementById("sportbuddies"), buddy)
        mount(dom.document.getElementById("searchbuddies"), search)

      case Failure(reason) =>
        println("Failed bitch")
    }


  }

}
