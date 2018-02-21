import org.scalatest.{AsyncWordSpec, Matchers}
import freestyle.free._
import freestyle.free.implicits._
import cats._
import cats.implicits._
import freestyle.free.effects.error.ErrorM
import freestyle.tagless._

import scala.concurrent.{ExecutionContext, Future}

class FreeStyleEffectsTests extends AsyncWordSpec with Matchers {

  implicit override def executionContext = ExecutionContext.Implicits.global


  "Option Freestyle integration for @free algebras" should {

    import freestyle.free.effects.option._
    import freestyle.free.effects.option.implicits._

    import cats.instances.option._
    import cats.mtl.implicits._

    "allow an Option to be interleaved inside a program monadic flow" in {
      val ex = new RuntimeException("BOOM")
      sealed trait CustomError
      case object Custom1 extends CustomError

      import freestyle.free.effects.either

      val e = either[CustomError]

      def program(implicit op: OptionM[OptionM.Op], eop: ErrorM[ErrorM.Op]) =
        for {
          a <- FreeS.pure(1)
          b <-Option(1).liftFS
          c <- FreeS.pure(1)
        } yield a + b + c

      program.interpret[Option] shouldBe Some(3)
    }

    "allow an Option to shortcircuit inside a program monadic flow" in {
      def program[F[_] : OptionM] =
        for {
          a <- FreeS.pure(1)
          b <- OptionM[F].none[Int]
          c <- FreeS.pure(1)
        } yield a + b + c

      program[OptionM.Op].interpret[Option] shouldBe None
    }

    "allow an Option to be interleaved inside a program monadic flow using syntax" in {
      def program[F[_] : OptionM] =
        for {
          a <- FreeS.pure(1)
          b <- Option(1).liftFS
          c <- FreeS.pure(1)
        } yield a + b + c

      program[OptionM.Op].interpret[Option] shouldBe Some(3)
    }
  }

  "Validation integration" should {
    import freestyle.free.effects._

    import cats.data.{State, StateT}
    import cats.instances.future._
    import cats.instances.list._
    import cats.mtl.implicits._

    // Custom error types

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

    "valid" in {
      def program[F[_] : vl.ValidationM] =
        for {
          _ <- FreeS.pure(1)
          b <- vl.ValidationM[F].valid(42)
          _ <- FreeS.pure(1)
        } yield b

      program[vl.ValidationM.Op].interpret[Logger].runEmpty map {
        _ shouldBe Tuple2(List(), 42)
      }
    }

    "invalid" in {
      def program[F[_] : vl.ValidationM] =
        for {
          b <- vl.ValidationM[F].valid(42)
          _ <- vl.ValidationM[F].invalid(NotValid("oh"))
          _ <- vl.ValidationM[F].invalid(MissingFirstName)
          _ <- FreeS.pure(1)
        } yield {
          println("Still executing business logic")
          b
        }

      val errors = List(NotValid("oh"), MissingFirstName)
      program[vl.ValidationM.Op].interpret[Logger].runEmpty map {
        _ shouldBe Tuple2(errors, 42)
      }
    }

    "errors" in {
      val expectedErrors = List(NotValid("oh"), NotValid("no"))

      def program[F[_] : vl.ValidationM] =
        for {
          b <- vl.ValidationM[F].valid(42)
          _ <- vl.ValidationM[F].invalid(NotValid("oh"))
          _ <- vl.ValidationM[F].invalid(NotValid("no"))
          _ <- FreeS.pure(1)
          actualErrors <- vl.ValidationM[F].errors
        } yield actualErrors == expectedErrors

      program[vl.ValidationM.Op].interpret[Logger].runEmpty map {
        _ shouldBe Tuple2(expectedErrors, true)
      }
    }

    "fromEither" in {
      import cats.syntax.either._

      val expectedErrors = List(MissingFirstName)

      def program[F[_] : vl.ValidationM] =
        for {
          a <- vl.ValidationM[F].fromEither(Right(42))
          b <- vl
            .ValidationM[F]
            .fromEither(Either.left[ValidationException, Unit](MissingFirstName))
        } yield a

      program[vl.ValidationM.Op].interpret[Logger].runEmpty.map {
        _ shouldBe Tuple2(expectedErrors, Right(42))
      }
    }

    "fromValidatedNel" in {
      import cats.data.Validated

      def program[F[_] : vl.ValidationM] =
        for {
          a <- vl.ValidationM[F].fromValidatedNel(Validated.valid(42))
          b <- vl
            .ValidationM[F]
            .fromValidatedNel(
              Validated.invalidNel[ValidationException, Unit](MissingFirstName)
            )
        } yield a

      program[vl.ValidationM.Op].interpret[Logger].runEmpty.map {
        _ shouldBe Tuple2(List(MissingFirstName), Validated.valid(42))
      }
    }

    "syntax" in {
      def program[F[_] : vl.ValidationM] =
        for {
          a <- 42.liftValid
          b <- MissingFirstName.liftInvalid
          c <- NotValid("no").liftInvalid
        } yield a

      val expectedErrors = List(MissingFirstName, NotValid("no"))
      program[vl.ValidationM.Op].interpret[Logger].runEmpty.map {
        _ shouldBe Tuple2(expectedErrors, 42)
      }
    }
  }

  "Traverse integration" should {

    import freestyle.free.effects._
    import cats.instances.list._

    val list = traverse.list
    import list._, list.implicits._

    "fromTraversable" in {
      def program[F[_] : TraverseM] =
        for {
          a <- TraverseM[F].fromTraversable(1 :: 2 :: 3 :: Nil)
          b <- FreeS.pure(a + 1)
        } yield b

      program[TraverseM.Op].interpret[List] shouldBe List(2, 3, 4)
    }

    "empty" in {
      def program[F[_] : TraverseM] =
        for {
          _ <- TraverseM[F].empty[Int]
          a <- TraverseM[F].fromTraversable(1 :: 2 :: 3 :: Nil)
          b <- FreeS.pure(a + 1)
          c <- FreeS.pure(b + 1)
        } yield c

      program[TraverseM.Op].interpret[List] shouldBe Nil
    }

    "syntax" in {
      def program[F[_] : TraverseM] =
        for {
          a <- (1 :: 2 :: 3 :: Nil).liftFS
          b <- FreeS.pure(a + 1)
          c <- FreeS.pure(b + 1)
        } yield c

      program[TraverseM.Op].interpret[List] shouldBe List(3, 4, 5)
    }

  }
}