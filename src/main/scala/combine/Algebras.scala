package combine

import akka.actor.Actor.Receive
import akka.actor.{ActorContext, ActorRef, Props}
import freestyle._

/**
  * Created by abdhesh on 26/04/17.
  */
object Algebras {

  case class Event(event: String)

  @free trait LoggerOp {
    def debug(msg: String, o: List[Event]): FS[Unit]

    def info(msg: String, o: List[Event]): FS[Unit]

    def warn(msg: String, o: List[Event]): FS[Unit]

  }

  @free trait ActorOp {
    def send(a: Any, to: ActorRef): FS[Unit]

    def sendToActorSelection(a: Any, context: ActorContext, path: String): FS[Unit]

    def tellFrom(a: Any, to: ActorRef, from: ActorRef): FS[Unit]

    def timeOut(context: ActorContext, time: Int): FS[Unit]

    def transitionState(context: ActorContext, state: () => Receive): FS[Unit]

    def stopActor(context: ActorContext, victim: ActorRef): FS[Unit]

    def createActor(props: Props, name: String, context: ActorContext): FS[ActorRef]

  }

  @free trait DisplayOp {
    def send(a: Any, to: ActorRef): FS[Unit]
  }

}
