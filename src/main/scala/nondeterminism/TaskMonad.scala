package nondeterminism

import cats.Monad
import monix.eval.Task

/**
  * Created by abdhesh on 10/04/17.
  */
object TaskMonad {
  implicit val taskMonad = new Monad[Task] {
    override def flatMap[A, B](fa: Task[A])(f: (A) => Task[B]): Task[B] =
      fa.flatMap(f)

    override def tailRecM[A, B](a: A)(f: (A) => Task[Either[A, B]]): Task[B] = {
      f(a).flatMap(_ match {
        case Right(eb) => Task.now(eb)
        case Left(ea) => tailRecM(ea)(f)
      })
    }

    override def pure[A](x: A): Task[A] = Task.now(x)
  }
}
