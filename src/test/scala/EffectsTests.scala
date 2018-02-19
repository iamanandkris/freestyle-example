import cats.data.{StateT, Validated}
import org.scalatest._


import scala.concurrent.Future

class EffectsTests extends FlatSpec with Matchers {

  import freestyle.free._
  import freestyle.free.implicits._
  import freestyle.free.effects.validation
  import freestyle.free.effects.option.implicits._
  import cats.data.State
  import cats.implicits._
  import cats.mtl.implicits._


  type ValidationResult[A] = State[List[ValidationError], A]
  val vl = validation[ValidationError]

  def programValid[F[_] : vl.ValidationM] =
    for {
      a <- FreeS.pure(1)
      b <- vl.ValidationM[F].valid(1)
      c <- FreeS.pure(1)
    } yield a + b + c

  import vl.implicits._

  def programInvalid[F[_] : vl.ValidationM] =
    for {

      a <- FreeS.pure(1)
      d <- vl.ValidationM[F].invalid(NotValid("oh no"))
      b <- FreeS.pure(1)
    } yield {
      println(s"Still Running ${d}")
      a + b
    }

  def programErrors[F[_] : vl.ValidationM] =
    for {
      _ <- vl.ValidationM[F].invalid(NotValid("oh no"))
      errs <- vl.ValidationM[F].errors
      _ <- vl.ValidationM[F].invalid(NotValid("this won't be in errs"))
    } yield errs

  def programFromValidatedNel[F[_] : vl.ValidationM] =
    for {
      a <- vl.ValidationM[F].fromValidatedNel(
        Validated.Valid(42)
      )
      b <- vl.ValidationM[F].fromValidatedNel(
        Validated.invalidNel[ValidationError, Unit](NotValid("oh no"))
      )
      c <- vl.ValidationM[F].fromValidatedNel(
        Validated.invalidNel[ValidationError, Unit](NotValid("another error!"))
      )
    } yield {
      println(s":::::::::::::::::${a}::${b}::${c}")
      a
    }

  def programFuture[F[_] : vl.ValidationM] =
    for {
      c <- FreeS.pure(1)
      b <- vl.ValidationM[F].valid(42)
      _ <- vl.ValidationM[F].invalid(NotValid("NOt"))
      d <- FreeS.pure(1)
    } yield {
      println(s"Running ${c + d}")
      b
    }

  sealed trait ValidationError

  case class NotValid(explanation: String) extends ValidationError

  "Valid" should "success test" in {
    val result = programValid[vl.ValidationM.Op].interpret[ValidationResult].runEmpty
    println(result.value)
  }

  "InValid" should "success test" in {
    val result = programInvalid[vl.ValidationM.Op].interpret[ValidationResult].runEmpty
    println(result.value)
  }

  "Errors" should "success test" in {
    val result = programErrors[vl.ValidationM.Op].interpret[ValidationResult].runEmpty
    println(result.value)
  }

  "ValidatedNel" should "success test" in {
    val result = programFromValidatedNel[vl.ValidationM.Op].interpret[ValidationResult].runEmpty
    println(result.value)
  }

  "programFuture" should "success test" in {
    type ValidationResult1[A] = State[List[ValidationError], A]
    val result = programFuture[vl.ValidationM.Op].interpret[ValidationResult1].runEmpty
    println(result.value)
  }

  it should "allow an Either to be interleaved inside a program monadic flow" in {
    import scala.concurrent.ExecutionContext.Implicits.global
    import freestyle.free.effects._

    import cats.data.{State, StateT}
    import cats.instances.future._
    import cats.instances.list._
    import cats.mtl.implicits._
    sealed trait ValidationException {
      def explanation: String
    }
    case class NotValid(explanation: String) extends ValidationException
    case object MissingFirstName extends ValidationException {
      val explanation = "The first name is missing"
    }

    type Errors = List[ValidationException]

    // Validation for custom errors

    val vl = validation[ValidationException]
    import vl.implicits._

    // Runtime

    type Logger[A] = StateT[Future, Errors, A]
    def program[F[_]: vl.ValidationM] =
      for {
        _ <- FreeS.pure(1)
        b <- vl.ValidationM[F].valid(42)
        _ <- vl.ValidationM[F].invalid(MissingFirstName)
        _ <- FreeS.pure(1)
      } yield {
        println(":::::")
        b
      }

    program[vl.ValidationM.Op].interpret[Logger].runEmpty map { _ shouldBe Tuple2(List(), 42) }
  }
}