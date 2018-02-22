import Algebra.ValidationResult
import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, Validated}
import collision.{B, C, F, X}
import org.scalatest.{AsyncWordSpec, Matchers}
import freestyle.free._
import freestyle.free.implicits._
import cats.implicits._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.{ExecutionContext, Future}

class ValidationFreeStyle extends AsyncWordSpec with Matchers with ScalaFutures {

  type ValidationResult

  implicit override def executionContext = ExecutionContext.Implicits.global

  implicit val bHandler = new B.Handler[Future] {
    override protected[this] def x: Future[Int] = Future.successful(23)
  }
  implicit val cHandler = new C.Handler[Future] {
    override protected[this] def x: Future[Int] = Future.successful(34)
  }
  implicit val fHandler = new F.Handler[Future] {
    override protected[this] def validate: Future[Validated[NonEmptyList[Exception], String]] =
      Future.successful(Validated.invalidNel(new Exception("Failed")))
  }

  "Mixing freestyle and cats validation" should {
    val result = X[X.Op].program.interpret[Future]
    whenReady(result) {
      case r => print(r)
    }

  }

}
