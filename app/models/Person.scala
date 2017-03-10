package models

case class PersonLogin(
                        email: String,
                        password: String
                        )

case class PersonSignup(
                       firstName: String,
                       lastName: String,
                       email: String,
                       password: String,
                       rePassword: String
                       )