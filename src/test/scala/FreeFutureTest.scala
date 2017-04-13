import org.scalatest.{Matchers, WordSpec}
import freestyle._
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
    val validator     = parValidation.exec[ParValidator]

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
    }yield (one)

    val validator     = k.exec[ParValidator]

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
    }yield (one)

    val validator     = k.exec[ParValidator]

    Await.result(validator.run("a"), Duration.Inf) shouldBe (false, false)
  }

}
