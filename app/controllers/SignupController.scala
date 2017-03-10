package controllers

import com.google.inject.Inject
import models.PersonSignup
import services.PersonInfo
import com.google.inject.Inject
import models.PersonSignup
import play.api.cache.CacheApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.{AddUser, MD5, PersonInfo}

class SignupController @Inject()(personObj: PersonInfo) extends Controller {

  def showProfile(email: String) = Action { implicit request =>
    request.session.get("email").map { person =>

      val data: List[PersonSignup] = personObj.getPersonData(email)

      Ok(views.html.profile("My profile")(data))
    }.getOrElse {
      Unauthorized("Oops, Connection Terminated!! ")
    }
  }

  def signUp = Action { implicit request =>
    Ok(views.html.signup("Sign Up"))
  }

  val signupData  : Form[PersonSignup] = Form(
    mapping(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
      "rePassword" -> nonEmptyText
    )(PersonSignup.apply)(PersonSignup.unapply)
  )

  def signupPost = Action { implicit request =>
  signupData.bindFromRequest.fold(
    formWithErrors => {
      println(formWithErrors)
      Redirect(routes.SignupController.signUp()).flashing("meassage" -> "Invalid Data, Try again")
    },
    formData => {

      val getuser: List[PersonSignup] = personObj.getPersonData(formData.email)

      if (getuser.isEmpty) {
        if (formData.password == formData.rePassword) {

          val encrypted = formData.copy(password = MD5.hash(formData.password))

          AddUser.listOfPerson.append(encrypted)

          Redirect(routes.SignupController.showProfile(encrypted.email)).withSession("email" -> formData.email).flashing("message" -> "Registration Successful")
        }
        else
          Redirect(routes.HomeController.index()).flashing("matcherror" -> "Pasword dosent Match")
      }
      else
        Redirect(routes.HomeController.index()).flashing("emailexists" -> "email already exists")
    }
  )
}

}
