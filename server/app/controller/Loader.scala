//package controller
//
//import models.Buddies
//import play.api.routing.Router
//import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
//import play.core.ApplicationProvider
//import router.Routes
//import services.BuddyService
//
//class Loader extends ApplicationLoader {
//  def load(context: ApplicationLoader.Context): Application = {
//    LoggerConfigurator(context.environment.classLoader).foreach {
//      _.configure(context.environment)
//    }
//    new Components(context).application
//  }
//}
//
//class Components(context: ApplicationLoader.Context) extends BuiltInComponentsFromContext(context) {
//  val counterCtl = new ApplicationCtl
//  val assetsCtl = new controllers.Assets(httpErrorHandler)
//  val router: Router = new Routes(httpErrorHandler, counterCtl, assetsCtl)
//}