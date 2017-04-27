package combine

import freestyle.module
import combine.Algebras._
import freestyle._
import freestyle.implicits._
/**
  * Created by abdhesh on 26/04/17.
  */
object Module {

  @module trait FinalApp[F[_]] {
    val loggerOp: LoggerOp[F]
    val actorOp: ActorOp[F]
    val displayOp: DisplayOp[F]
  }

  @module trait Module1[F[_]]{
    val loggerOp: LoggerAOp[F]
    val actorOp: ActorOp[F]
  }

  @module trait Module2[F[_]]{
    val actorOp: ActorOp[F]
    val displayOp: DisplayOp[F]
  }

  @module trait Module3[F[_]]{
    val module1: Module1[F]
    val module2: Module2[F]
  }
}
