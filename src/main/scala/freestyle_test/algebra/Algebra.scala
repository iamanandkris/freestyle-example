package freestyle_test.algebra

import cats.Monad
import freestyle._

/**
  * Created by abdhesh on 12/04/17.
  */
object Algebra {

  @free trait Parser {
    def parse(text: String): FS[Option[(List[String], String)]]
  }

}
