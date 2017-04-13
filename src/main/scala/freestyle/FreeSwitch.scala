package freestyle

import freestyle.algebra.Algebra.Parser

/**
  * Created by abdhesh on 12/04/17.
  */
trait FreeSwitch {

  def parseMessage[F[_]](text: String)(implicit app: Parser[F]): FreeS[F, Option[(List[String], String)]]
}
