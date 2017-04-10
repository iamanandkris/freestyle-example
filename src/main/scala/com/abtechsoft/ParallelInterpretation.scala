package com.abtechsoft

import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing.Validation

/**
  * Created by abdhesh on 10/04/17.
  */
object ParallelInterpretation extends App {

  import freestyle._
  import cats.data.Kleisli
  import cats.implicits._
  import scala.concurrent._
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global
  import freestyle.implicits._

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

  val parValidation = (validation.minSize(3) |@| validation.hasNumber).map(_ :: _ :: Nil)
  // parValidation: freestyle.FreeS.Par[Validation.Op,List[Boolean]] = FreeApplicative(...)

  val validator = parValidation.exec[ParValidator].run("abc1")
  println(Await.result(validator, Duration(2, SECONDS)))
}
