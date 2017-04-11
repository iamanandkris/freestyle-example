package nondeterminism

import cats.Applicative
import cats.data.Kleisli

/**
  * Created by abdhesh on 11/04/17.
  */
object KleisliImplicits {

  implicit def kleisliPure[F[_], A, B](a: => B)(implicit F: Applicative[F]): Kleisli[F, A, B] = Kleisli.pure(a)

  implicit def kleisliMix[F[_], A, B](run: A => F[B]): Kleisli[F, A, B] = Kleisli(run)

  implicit def kleisliMixUnit[F[_], A, B](run: => F[B]): Kleisli[F, A, B] = Kleisli.lift(run)
}
