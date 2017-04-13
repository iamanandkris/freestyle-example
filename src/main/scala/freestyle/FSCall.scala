package freestyle

import akka.actor.Props
import freestyle.algebra.Algebra

/**
  * Created by abdhesh on 12/04/17.
  */
class FSCall extends FreeSwitchActor with FreeSwitch {

  import freestyle.algebra.Interpreters._

  override def parseMessage[F[_]](text: String)(implicit app: Algebra.Parser[F]): FreeS[F,Option[(List[String], String)]] = {
    for {
      result <- app.parse(text)
    } yield result
  }
}

object FSCall {
  def props: Props = Props(new FSCall)

  def name: String = "call-actor"
}
