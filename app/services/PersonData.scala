package services

import java.io.Serializable

import com.google.inject.Inject
import models.PersonSignup
import play.api.cache
import play.api.cache.CacheApi

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
trait PersonInfo {
  def getPersonData(email: String) : List[PersonSignup]
}
class PersonData @Inject()(cache: CacheApi)extends PersonInfo {

    override def getPersonData(email : String): List[PersonSignup] = {
        val users = AddUser.listOfPerson
        val user = users.filter(_.email == email)
        user.toList

    }


}
