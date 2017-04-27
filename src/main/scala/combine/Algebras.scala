package combine

import akka.actor.Actor.Receive
import akka.actor.{ActorContext, ActorRef, Props}
import freestyle.{FreeS, free}

/**
  * Created by abdhesh on 26/04/17.
  */
object Algebras {

  case class Event(event: String)

  @free trait LoggerOp[A[_]] {
    def debug(msg: String, o: List[Event]): FreeS[A, Unit]

    def info(msg: String, o: List[Event]): FreeS[A, Unit]

    def warn(msg: String, o: List[Event]): FreeS[A, Unit]

  }

  @free trait LoggerAOp[A[_]] {
    def debug(msg: String, o: List[Event]): FreeS[A, String]

    def info(msg: String, o: List[Event]): FreeS[A, String]

    def warn(msg: String, o: List[Event]): FreeS[A, String]

  }

  @free trait ActorOp[A[_]] {
    def send(a: Any, to: ActorRef): FreeS[A, Unit]

    def sendToActorSelection(a: Any, context: ActorContext, path: String): FreeS[A, Unit]

    def tellFrom(a: Any, to: ActorRef, from: ActorRef): FreeS[A, Unit]

    def timeOut(context: ActorContext, time: Int): FreeS[A, Unit]

    def transitionState(context: ActorContext, state: () => Receive): FreeS[A, Unit]

    def stopActor(context: ActorContext, victim: ActorRef): FreeS[A, Unit]

    def createActor(props: Props, name: String, context: ActorContext): FreeS[A, ActorRef]

  }

  @free trait DisplayOp[A[_]] {
    def send(a: Any, to: ActorRef): FreeS[A, Unit]
  }

}
