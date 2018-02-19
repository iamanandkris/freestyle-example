import cats.Id
import freestyle.free._
import cats.implicits._
import freestyle.free.logging._
import freestyle.free.implicits._
import freestyle.free.logging.LoggingM
import org.scalatest.{FlatSpec, Matchers}

class CatsValidation extends FlatSpec with Matchers {
import cats.implicits._
  import cats.syntax._
  "Cats validation" should "work with freestyle" in {
    val result = FormValidatorNel.validateForm(
      username = "Joe",
      password = "Passw0r$1234",
      firstName = "John",
      lastName = "Doe",
      age = 21
    )

    println(result)
  }


  "Cats invalid" should "work with freestyle" in {
    val result = FormValidatorNel.validateForm(
      username = "Joe%%%",
      password = "password",
      firstName = "John",
      lastName = "Doe",
      age = 21
    )
    println(result)
  }

  "Validation with freestyle" should "run" in {
    import freestyle.free._
    import freestyle.free.implicits._
    import cats.implicits._
    import Program._
    import Module._
    import Handlers._
    program(username = "Joe",
      password = "Passw0r$1234",
      firstName = "John",
      lastName = "Doe",
      age = 21).interpret[Id]
  }

  "InValide with freestyle" should "run" in {
    import freestyle.free._
    import freestyle.free.implicits._
    import cats.implicits._
    import Program._
    import Module._
    import Handlers._
    program(username = "Joe%%%",
      password = "password",
      firstName = "John",
      lastName = "Doe",
      age = 21).interpret[Id]
  }

}

