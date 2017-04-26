package combine

import akka.actor.Actor.Receive
import akka.actor.{ActorContext, ActorRef, Props}
import cats.{Id, ~>}
import combine.Algebras._
import combine.Module._
import scala.concurrent.duration._
import scala.concurrent.duration.Duration

/**
  * Created by abdhesh on 26/04/17.
  */
object Interpreters {

  implicit val StatelessLoggerInterpreter = new LoggerOp.Handler[Id] {
    def debug(msg: String, o: List[Event]): Id[Unit] = println(s"[Debug] : ${msg}")

    def info(msg: String, o: List[Event]): Id[Unit] = println(s"[Info] : ${msg}")

    def warn(msg: String, o: List[Event]): Id[Unit] = println(s"[Warn] : ${msg}")

    def error(msg: String, o: List[Event]): Id[Unit] = println(s"[Error] : ${msg}")
  }

  implicit val StatelessActorOpsInterpreter = new ActorOp.Handler[Id] {
    def send(a: Any, to: ActorRef): Id[Unit] = to ! a

    def sendToActorSelection(a: Any, context: ActorContext, path: String): Id[Unit] = context.actorSelection(path) ! a

    def tellFrom(a: Any, to: ActorRef, from: ActorRef): Id[Unit] = to tell(a, from)

    def timeOut(context: ActorContext, time: Int): Id[Unit] = {
      if (time == 0) {
        context.setReceiveTimeout(Duration.Undefined)
      }
      else {
        context.setReceiveTimeout(time seconds)
      }
    }

    def transitionState(context: ActorContext, state: () => Receive): Id[Unit] = context.become(state())

    def stopActor(context: ActorContext, victim: ActorRef): Id[Unit] = context.stop(victim)

    def createActor(props: Props, name: String, context: ActorContext): Id[ActorRef] = context.actorOf(props, name)

  }
}

object CombineInterpreters{
  import Interpreters._
  implicit val loggerAndActorInterpreter: FinalApp.Op ~> Id = implicitly[FinalApp.Op ~> Id]
  //implicit val loggerAndActorInterpreter: FinalApp.Op ~> Id = Interpreters.StatelessLoggerInterpreter or Interpreters.StatelessActorOpsInterpreter
}
