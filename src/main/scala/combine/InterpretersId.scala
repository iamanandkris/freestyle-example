package combine

import akka.actor.Actor.Receive
import akka.actor.{ActorContext, ActorRef, Props}
import cats.{Id, ~>}
import combine.Algebras._
import freestyle.{FreeS, module}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import combine.Module._
import scala.concurrent.duration._
import scala.concurrent.duration.Duration

/**
  * Created by abdhesh on 26/04/17.
  */
object InterpretersId {

   implicit val StatelessActorOpsInterpreter = new ActorOp.Handler[Id] {
    def send(a: Any, to: ActorRef): Id[Unit] = println(s"${to.toString()} bang ${a}")

    def sendToActorSelection(a: Any, context: ActorContext, path: String): Id[Unit] = println("test")//context.actorSelection(path) ! a

    def tellFrom(a: Any, to: ActorRef, from: ActorRef): Id[Unit] = println(s"${to.toString()} tell ${a} from ${from.toString()}")//to tell(a, from)

    def timeOut(context: ActorContext, time: Int): Id[Unit] = println("test")/*{
      if (time == 0) {
        context.setReceiveTimeout(Duration.Undefined)
      }
      else {
        context.setReceiveTimeout(time seconds)
      }
    }*/

    def transitionState(context: ActorContext, state: () => Receive): Id[Unit] = println("test")//context.become(state())

    def stopActor(context: ActorContext, victim: ActorRef): Id[Unit] = println("test")//context.stop(victim)

    def createActor(props: Props, name: String, context: ActorContext): Id[ActorRef] = {println("test");ActorRef.noSender}//context.actorOf(props, name)

  }


   implicit val StatelessLoggerInterpreter = new LoggerOp.Handler[Id] {
    def debug(msg: String, o: List[Event]): Id[Unit] = println(s"[Debug] : ${msg}")

    def info(msg: String, o: List[Event]): Id[Unit] = println(s"[Info] : ${msg}")

    def warn(msg: String, o: List[Event]): Id[Unit] = println(s"[Warn] : ${msg}")

    def error(msg: String, o: List[Event]): Id[Unit] = println(s"[Error] : ${msg}")
  }

  implicit val StatelessALoggerInterpreter = new LoggerAOp.Handler[Id] {
    def debug(msg: String, o: List[Event]): Id[String] = {println(s"[Debug] : ${msg}") ; msg+"-debug"}

    def info(msg: String, o: List[Event]): Id[String] = {println(s"[Info] : ${msg}") ; msg+"-info"}

    def warn(msg: String, o: List[Event]): Id[String] = {println(s"[Warn] : ${msg}") ; msg+"-warn"}

    def error(msg: String, o: List[Event]): Id[String] = {println(s"[Error] : ${msg}") ; msg+"-error"}
  }

   implicit val DisplayOpInterpreter = new DisplayOp.Handler[Id] {
    def send(a: Any, to: ActorRef): Id[Unit] = println("send function::::" + a)
  }
}

