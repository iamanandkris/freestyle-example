package combine

import freestyle.module
import combine.Algebras._
import freestyle._
import freestyle.implicits._
/**
  * Created by abdhesh on 26/04/17.
  */
object Module {

  @module trait FinalApp {
    val loggerOp: LoggerOp
    val actorOp: ActorOp
    val displayOp: DisplayOp
  }

}
