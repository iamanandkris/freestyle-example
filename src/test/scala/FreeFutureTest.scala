import cats.Monad
import cats.data.Kleisli
import org.scalatest.{Matchers, WordSpec}
import freestyle._
import org.scalactic.Requirements

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration


/**
  * Created by abdhesh on 13/04/17.
  */
class FreeFutureTest extends WordSpec with Matchers {

  "program with for-expression running in determinism way because of `tupled.freeS` " in {
    import cats.implicits._
    import scala.concurrent.ExecutionContext.Implicits.global
    import Algebra._
    import Interpreters._
    import freestyle.nondeterminism._
    import freestyle.implicits._

    def program[F[_]](implicit app: Validation[F]) = {
      for {
        validationResult <- (app.minSize(3) |@| app.hasNumber).tupled.freeS
        (vM, hN) = validationResult
      } yield vM :: hN :: Nil
    }

    val validator = program[Validation.Op].exec[ParValidator]
    Await.result(validator.run("a"), Duration.Inf) shouldBe List(false, false)
  }

  "program without for-expression running in nondeterminism way even without importing `freestyle.nondeterminism._`" in {
    import cats.implicits._
    import scala.concurrent.ExecutionContext.Implicits.global
    import freestyle.implicits._
    import Algebra._
    import Interpreters._


    val validation = Validation[Validation.Op]
    import validation._
    val parValidation = (minSize(3) |@| hasNumber).map(_ :: _ :: Nil)
    val validator = parValidation.exec[ParValidator]

    Await.result(validator.run("a"), Duration.Inf) shouldBe List(false, false)
  }

  "program with for-expression running in nondeterminism style" in {
    import cats.implicits._
    import scala.concurrent.ExecutionContext.Implicits.global
    import Algebra._
    import Interpreters._
    import freestyle.nondeterminism._
    import freestyle.implicits._

    val validation = Validation[Validation.Op]
    import validation._
    val k = for {
      one <- (minSize(3) |@| hasNumber).map(_ :: _ :: Nil)
    } yield (one)

    val validator = k.exec[ParValidator]

    Await.result(validator.run("a"), Duration.Inf) shouldBe List(false, false)
  }

  "program with for-expression running in determinism style because of `tupled.freeS`" in {
    import cats.implicits._
    import scala.concurrent.ExecutionContext.Implicits.global
    import Algebra._
    import Interpreters._
    import freestyle.nondeterminism._
    import freestyle.implicits._

    val validation = Validation[Validation.Op]
    import validation._
    val k = for {
      one <- (minSize(3) |@| hasNumber).tupled.freeS
    } yield (one)

    val validator = k.exec[ParValidator]

    Await.result(validator.run("a"), Duration.Inf) shouldBe(false, false)
  }

  "fix parallel issue" in {
    import freestyle.implicits._
    import freestyle._
    import cats.implicits._
    import scala.concurrent.ExecutionContext.Implicits.global
    import Algebra._
    import Interpreters._
    import freestyle.nondeterminism._

    val validation = Validation[Validation.Op]
    
    import validation._

    val program: FreeS[Validation.Op, (Boolean, Boolean)] =
      (minSize(3) |@| hasNumber).tupled.freeS

    // a bit more work than  foo.exec[ParValidator] ...
    val parValidatorMonad: Monad[ParValidator] = KleisliMonad.kleisliMonad[Future, String]
    val parInterpreter: ParInterpreter[Validation.Op, ParValidator] =
      interpretAp(parValidatorMonad, implicitly[Algebra.Validation.Handler[ParValidator]])


    val result: ParValidator[(Boolean, Boolean)] = program.foldMap(parInterpreter)
    import scala.concurrent.duration._
    val output = Await.result(result.run("test"), Duration(7, SECONDS))
   output shouldBe (true,false)
  }
}

object KleisliMonad {

  def kleisliMonad[F[_], A](implicit F: Monad[F]) = new Monad[Kleisli[F, A, ?]] {
    def flatMap[B, C](fa: Kleisli[F, A, B])(f: B => Kleisli[F, A, C]): Kleisli[F, A, C] =
      fa.flatMap(f)

    def tailRecM[B, C](b: B)(f: B => Kleisli[F, A, Either[B, C]]): Kleisli[F, A, C] =
      Kleisli[F, A, C]({ a =>
        F.tailRecM(b) {
          f(_).run(a)
        }
      })

    def pure[B](x: B): Kleisli[F, A, B] =
      Kleisli.pure[F, A, B](x)

    override def ap[B, C](f: Kleisli[F, A, B => C])(fa: Kleisli[F, A, B]): Kleisli[F, A, C] =
      fa.ap(f)

    override def product[B, C](fb: Kleisli[F, A, B], fc: Kleisli[F, A, C]): Kleisli[F, A, (B, C)] =
      Kleisli(a => F.product(fb.run(a), fc.run(a)))

    override def map[B, C](fa: Kleisli[F, A, B])(f: B => C): Kleisli[F, A, C] =
      fa.map(f)
  }
}