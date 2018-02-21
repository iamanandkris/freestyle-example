import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import org.scalatest.{AsyncWordSpec, FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import freestyle.free._
import freestyle.free.implicits._
import cats.implicits._
import cats.data.Validated._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class ValidationSpec extends FlatSpec with Matchers with ScalaFutures {


  @free trait ValidationM {
    def validateOrBreak[E, A](validations: ValidatedNel[E, A]): FS[A]
  }


  @free trait Numbers {
    def moreThan(a: Int): FS[Int]
  }

  @free trait Strings {
    def concat(s: String): FS[String]
  }

  @free trait Validation {
    def validation(s: String): FS[ValidationResult[String]]
  }

  @module trait Ops {
    val int: Numbers
    val string: Strings
    val v: ValidationM
    val v1: Validation
  }

  type ValidationResult[A] = ValidatedNel[Any, A]


  implicit val validationHandler = new ValidationM.Handler[ValidationResult] {
    override protected[this] def validateOrBreak[E, A](validations: ValidatedNel[E, A]): ValidationResult[A] = validations
  }

  class NumHandler(b: Int)(implicit executionContext: ExecutionContext) extends Numbers.Handler[ValidationResult] {
    override protected[this] def moreThan(a: Int): ValidationResult[Int] = {

      if (a > b) a.validNel else "Not more than".invalidNel
    }
  }

  implicit val v = new Validation.Handler[Future] {
    override def validation(s: String): Future[ValidationResult[String]] = Future.successful(Validated.invalidNel("Valid value"))
  }

  class StringHandler(ss: String)(implicit executionContext: ExecutionContext) extends Strings.Handler[Future] {
    override protected[this] def concat(s: String): Future[String] = Future.successful(ss + s)
  }


  implicit object nums extends NumHandler(5)

  case class ValidationError[E](errors: NonEmptyList[E]) extends Throwable

  implicit def validToFuture[E, A](validation: ValidationResult[A]): Future[A] = {
    validation match {
      case Valid(a) => Future.successful(a)
      case Invalid(e) => Future.failed(ValidationError(e))
    }
  }


  implicit def validHtoFutureH[Op[_], E](implicit validHandler: FSHandler[Op, ValidationResult]) = {
    new FSHandler[Op, Future] {
      override def apply[A](fa: Op[A]): Future[A] = validHandler(fa)
    }
  }

  implicit object strings extends StringHandler("")

  import freestyle.free.effects.validation


  def addPgrm(a: Int)(implicit op: Ops[Ops.Op]) = {
    import op._
    for {
      i <- int.moreThan(a)
      s <- string.concat(a.toString)

    } yield i.toString.concat(s)
  }

  "program" should "succeed" in {
    assert(
      Await.result(addPgrm(8).interpret[Future], Duration.Inf) == "88"
    )
  }
  it should "throw ValidationError" in {
    assertThrows[ValidationError[_]] {
      Await.result(addPgrm(3).interpret[Future], Duration.Inf)
    }
  }

  "mixed logic" should "work" in {
    def validate(numIn: Seq[String]): Validated[String, Seq[String]] = "Invalid".invalid

    def pgrm(implicit op: Ops[Ops.Op]): FreeS[Ops.Op, Seq[String]] = {
      for {
        numbers <- FreeS.pure(Seq("sgfdsfgd"))
        result <- op.v1.validation("")
        _ <- op.v.validateOrBreak(result)
        _ <- op.v.validateOrBreak(validate(numbers).toValidatedNel)
        _ <- op.string.concat("test")
      } yield numbers
    }

    whenReady(pgrm.interpret[Future]) {
      case r => println(r)
    }
  }


}
