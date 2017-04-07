package nondeterminism

/**
  * Created by abdhesh on 06/04/17.
  */
object ValidationApp extends App {

  import cats.data.Kleisli
  import cats.implicits._
  import scala.concurrent._
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global
  import freestyle.implicits._
  import freestyle.nondeterminism._
  import freestyle._
/*
  @free trait Validation[F[_]] {
    def minSize(n: Int): FreeS.Par[F, Boolean]

    def hasNumber: FreeS.Par[F, Boolean]
  }

  type ParValidator[A] = Kleisli[Future, String, A]
  // defined type alias ParValidator
  implicit val interpreter = new Validation.Handler[ParValidator] {
    def minSize(n: Int): ParValidator[Boolean] =
      Kleisli(s => Future(s.size >= n))

    def hasNumber: ParValidator[Boolean] =
      Kleisli(s => Future(s.exists(c => "0123456789".contains(c))))
  }
  val validation = Validation[Validation.Op]

  import validation._
  import scala.concurrent.duration._

  val parValidation = (minSize(3) |@| hasNumber).map(_ :: _ :: Nil)
  val validator = parValidation.exec[ParValidator]
  //val result = Await.result(validator, Duration(2, MILLISECONDS))
  println(validator)*/
}
