package freestyle_test.algebra

import cats.Monad
import freestyle.{FreeS, free}

/**
  * Created by abdhesh on 12/04/17.
  */
object Algebra {

  @free trait Parser[F[_]] {
    def parse(text: String): FreeS[F, Option[(List[String], String)]]
  }

}
