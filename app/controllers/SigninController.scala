package controllers

import javax.inject.Inject

import models.PersonLogin
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import services.{AddUser, MD5, PersonData}

class SigninController @Inject() extends Controller{

  val loginForm: Form[PersonLogin]= Form (
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    )(PersonLogin.apply)(PersonLogin.unapply)
  )



  def signInPost = Action { implicit request =>
    loginForm.bindFromRequest().fold(
      formWithErrors => {
        Redirect(routes.HomeController.index).flashing("message" -> "Invalid Data")
      },
      loginData => {
        val newUser = AddUser.listOfPerson
        val result = newUser.map( user =>
          if((user.email == loginData.email)&&(user.password == MD5.hash(loginData.password) )) true
          else
            false
        )

        if(result.contains(true))
          Redirect(routes.SignupController.showProfile(loginData.email)).withSession("email" -> loginData.email).flashing("message" -> "Login SuccessFul")
        else
          Redirect(routes.HomeController.index()).flashing("message"->"Incorrect username or password")
      }


    )
  }

}
