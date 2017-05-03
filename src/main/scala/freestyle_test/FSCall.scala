package freestyle_test

import akka.actor.Props
import freestyle.FreeS
import freestyle_test.algebra.Algebra
import freestyle_test.algebra.Algebra.Parser
import freestyle._

/**
  * Created by abdhesh on 12/04/17.
  */
class FSCall extends FreeSwitchActor with FreeSwitch {

  import freestyle_test.algebra.Interpreters._

  override def parseMessage[F[_]](text: String)(implicit app: Parser[F]): FreeS[F,Option[(List[String], String)]] = {
    for {
      result <- app.parse(text)
    } yield result
  }
}

object FSCall {
  def props: Props = Props(new FSCall)

  def name: String = "call-actor"
}
