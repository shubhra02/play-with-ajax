package controllers


import com.google.inject.Inject
import play.api._
import play.api.mvc._

class HomeController @Inject() extends Controller {


  def index = Action {
    Ok(views.html.signin("Welcome!"))
  }

  def logout = Action { implicit  request =>
    Ok(views.html.signin("Welcome!")).withNewSession.flashing("message" -> "You have successfully logged out!")
  }

}
