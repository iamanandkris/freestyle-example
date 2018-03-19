package abtechsoft.handlers

import abtechsoft.algebra.CommandAlgebra
import abtechsoft.programs.{Command, FindNumberResponse}
import cats.Applicative

import scala.concurrent.Future

object CommandAlgebraHandler {
  implicit def commandAlgebraHandler(implicit app: Applicative[Future]) = new CommandAlgebra.Handler[Future] {
    override protected[this] def create(data: List[FindNumberResponse]): Future[Command] = {
      Future.successful(Command(3232L))
    }

    override protected[this] def validation(data: List[(String, Long)]): Future[Unit] = {
      Future.unit
    }
  }
}
