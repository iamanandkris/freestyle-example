package combine

import akka.actor.ActorContext
import cats.Id
import cats.arrow.FunctionK
import combine.Algebras.Event
import freestyle.FreeS
import freestyle._
import freestyle.implicits._
import cats.implicits._

/**
  * Created by abdhesh on 26/04/17.
  */
object Programs {

  final def handleCommand[T[?], A <: Event](message: A,
                                            success: (A, ActorContext) => FreeS[T, List[Event]],
                                            failure: (A, ActorContext) => FreeS[T, List[Event]],
                                            logger: (String, List[Event]) => FreeS[T, List[Event]],
                                            timeOut: => FreeS[T, List[Event]]
                                           )(implicit context: ActorContext, interpreter: FunctionK[T, cats.Id]): List[Event] = {
    //you will get the compilation error when uncomment the code
    /*
    success(message,context).exec[Id](interpreter)
    logger("test message",List(message)).exec[Id](interpreter)
     */
    Nil
  }
}