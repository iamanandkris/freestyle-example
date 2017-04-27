package combine

import akka.actor.{ActorContext, ActorRef}
import cats.Id
import combine.Algebras.{DisplayOp, Event}
import freestyle.FreeS
import freestyle._
import freestyle.implicits._
import cats.implicits._
import combine.CombineInterpreters._
//import freestyle.implicits._

/**
  * Created by abdhesh on 26/04/17.
  */
object Programs {
  final def handleCommand[T[_], A <: Event](message: A,
                                            success: (A, ActorContext) => FreeS[T, List[Event]],
                                            failure: (A, ActorContext) => FreeS[T, List[Event]],
                                            logger: (String, List[Event]) => FreeS[T, List[Event]],
                                            timeOut: => FreeS[T, List[Event]]
                                           )(implicit context: ActorContext, interpreter: ParInterpreter[T, cats.Id]): List[Event] = {
    //you will get the compilation error when uncomment the code

    success(message, context).exec[Id]
    logger("test message", List(message)).exec[Id]
    Nil
  }


  import Module._

  def program(implicit app: DisplayOp[FinalApp.Op]): FreeS[FinalApp.Op, Unit] = {
    for {
      _ <- app.send("Test", ActorRef.noSender)
    } yield ()
  }

  def programWithApp[F[_]](implicit app: FinalApp[F]): FreeS[F, Unit] = {
    for {
      _ <- app.displayOp.send("Test", ActorRef.noSender)
    } yield ()
  }
}
