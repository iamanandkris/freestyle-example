package freestyle.algebra

import cats.Monad
import freestyle.{FreeS, free}

/**
  * Created by abdhesh on 12/04/17.
  */
object Algebra {

  @free trait Parser[F[_]] {
    def parse(text: String): FreeS[F, Option[(List[String], String)]]
  }

  /*@free trait Par[F[_]] {
    def parallel2[F[_],A[_], B[_]](free1: FreeS[F, A[_]], free2: FreeS[F, B[_]]): FreeS[F, (A, B)]
  }*/

}
