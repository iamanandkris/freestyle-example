package abtechsoft.handlers

import abtechsoft.algebra.Validation
import cats.Applicative
import cats.data.{EitherT, NonEmptyList, Validated, ValidatedNel}
import cats.data.Validated.{Invalid, Valid}
import com.sksamuel.elastic4s.http.{ElasticError, RequestFailure}
import freestyle.free.FSHandler

import scala.concurrent.Future

object ValidationHandler {

  case class ErrorWithContext[E](errors: NonEmptyList[E])

  type ValidationResult[A] = Validated[ErrorWithContext[Any], A]

  //type Target[A] = EitherT[Future, ErrorWithContext[Any], A]

  implicit def validToEither[A](validation: ValidationResult[A]): Either[ErrorWithContext[Any], A] = {
    validation match {
      case Valid(a) => Right(a)
      case Invalid(ee) => Left(ee)
    }
  }

  /*
    implicit def validHtoFutureH[Op[_]](implicit validHandler: FSHandler[Op, ValidationResult]): FSHandler[Op, Either] = {
      new FSHandler[Op, Either] {
        override def apply[A](fa: Op[A]): Either[ErrorWithContext[Any], A] = validHandler(fa)
      }
    }*/

  /*implicit def validationHandler(implicit app: Applicative[Future]) = new Validation.Handler[Target] {

    override def mapFailures[E, X](validated: ValidatedNel[E, X]): Target[X] = {
      validated match {
        case Valid(v) => EitherT.rightT[Future, RequestFailure](v)
        case Invalid(e) => EitherT.leftT[Future, X](RequestFailure(status = 0, None, Map.empty, ElasticError.fromThrowable(new Exception(""))))
      }
    }
  }*/
}
