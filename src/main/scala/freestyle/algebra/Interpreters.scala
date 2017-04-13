package freestyle.algebra

import cats.Monad
import cats.arrow.FunctionK
import cats.free.FreeApplicative
import freestyle.FreeS
import freestyle._

import scala.concurrent.{Await, Future}
import scala.util.{Success, Try}
import freestyle.algebra.Algebra._
import cats.implicits._

/**
  * Created by abdhesh on 12/04/17.
  */
object Interpreters {

  implicit val parserInterpreter = new freestyle.algebra.Algebra.Parser.Handler[Future] {
    def parse(text: String): Future[Option[(List[String], String)]] = {
      Future.successful(Some(text.split(",").toList -> "Un-parsed"))
    }
  }

  /*implicit val parOperations = new Par.Handler[Future] {

    def parallel2[H[_], A, B](free1: FreeS[Future, A], free2: FreeS[Future, B]): Future[(A, B)] = {
      implicit val intA: FunctionK[FreeApplicative[Future, ?], Future] = freestyle.implicits.interpretAp[Future, Future]
      implicit val intB: FunctionK[FreeApplicative[Future, ?], Future] = freestyle.implicits.interpretAp[Future, Future]
      val f1:Future[A] = free1.exec[Future]
      val f2:Future[B] = free2.exec[Future]
      f1.zip(f2)
    }
  }*/
}
