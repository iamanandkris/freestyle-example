import Algebra.FormValidation
import cats.{Id, Monad}
import cats.data.{Validated, ValidatedNel}


object Algebra {

  import freestyle.free.free
  import cats.data._
  import cats.data.Validated._
  import cats.implicits._

  type ValidationResult[A] = ValidatedNel[DomainValidation, A]

  @free trait Validation {
    def minSize(s: String, n: Int): FS[Boolean]

    def hasNumber(s: String): FS[Boolean]
  }

  @free trait Interaction {
    def tell(msg: String): FS[Unit]

    def ask(prompt: String): FS[String]
  }

  @free trait FormValidation {
    def validateUserName(userName: String): FS[ValidationResult[String]]

    def validatePassword(password: String): FS[ValidationResult[String]]

    def validateFirstName(firstName: String): FS[ValidationResult[String]]

    def validateLastName(lastName: String): FS[ValidationResult[String]]

    def validateAge(age: Int): FS[ValidationResult[Int]]
  }

}

object Module {

  import Algebra._
  import freestyle.free.module

  @module trait App {
    val v: Validation
    val I: Interaction
    val form: FormValidation
  }


}

object Handlers {

  import Algebra._
  import cats.data._
  import cats.data.Validated._
  import cats.implicits._

  implicit val validation = new Validation.Handler[Id] {
    override def hasNumber(s: String): Id[Boolean] = true

    override def minSize(s: String, n: Int): Id[Boolean] = true
  }
  implicit val interaction = new Interaction.Handler[Id] {
    override protected[this] def tell(msg: String): Id[Unit] = println(msg)

    override protected[this] def ask(prompt: String): Id[String] = "ask message"
  }
  implicit val formValidation = new FormValidation.Handler[Id] {
    override def validateFirstName(firstName: String): Id[ValidationResult[String]] =
      if (firstName.matches("^[a-zA-Z]+$")) firstName.validNel else FirstNameHasSpecialCharacters.invalidNel

    override def validateLastName(lastName: String): Id[ValidationResult[String]] =
      if (lastName.matches("^[a-zA-Z]+$")) lastName.validNel else LastNameHasSpecialCharacters.invalidNel

    override def validatePassword(password: String): Id[ValidationResult[String]] =
      if (password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) password.validNel
      else PasswordDoesNotMeetCriteria.invalidNel

    override def validateAge(age: Int): Id[ValidationResult[Int]] =
      if (age >= 18 && age <= 75) age.validNel else AgeIsInvalid.invalidNel

    override def validateUserName(userName: String): Id[ValidationResult[String]] =
      if (userName.matches("^[a-zA-Z0-9]+$")) userName.validNel else UsernameHasSpecialCharacters.invalidNel
  }
}

object Program {

  import Module._
  import freestyle.free._
  import cats.implicits._
  import freestyle.free.implicits._

  def program(username: String, password: String, firstName: String, lastName: String, age: Int)(implicit ap: App[App.Op]) = {
    import ap._
    import ap.form._
    for {
      userInput <- I.ask("Give me something with at least 3 chars and a number on it")
      validated <- (
        validateUserName(username),
        validatePassword(password),
        validateFirstName(firstName),
        validateLastName(lastName),
        validateAge(age)).mapN((a1, a2, a3, a4, a5) => (a1, a2, a3, a4, a5).mapN(RegistrationData.apply))
      valid <- (v.minSize(userInput, 3), v.hasNumber(userInput)).mapN(_ && _)
      _ <- if (valid)
        I.tell("awesomesauce!")
      else
        I.tell(s"$userInput is not valid")
      //_ <- log.debug("Program finished")
    } yield {
      println("validated:::" + validated)
    }
  }

  implicit class LiftValidationToMonad[E, A](result: ValidatedNel[E, A]) {
    def run[F[_]](implicit M: Monad[F]) = result match {
      case Validated.Valid(a) =>M.pure(a)
      case Validated.Invalid(a) =>M.pure(a)
    }
  }

}