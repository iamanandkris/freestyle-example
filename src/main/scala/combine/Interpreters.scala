package combine

import akka.actor.Actor.Receive
import akka.actor.{ActorContext, ActorRef, Props}
import cats.arrow.FunctionK
import cats.{Id, ~>}
import combine.Algebras.{DisplayOp, _}
import freestyle.{FreeS, ParInterpreter, module}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import combine.Module._
import freestyle.implicits._

import scala.concurrent.duration._
import scala.concurrent.duration.Duration

/**
  * Created by abdhesh on 26/04/17.
  */
object Interpreters {

  implicit val StatelessActorOpsInterpreter = new ActorOp.Handler[Future] {
    def send(a: Any, to: ActorRef): Future[Unit] = Future.successful(to ! a)

    def sendToActorSelection(a: Any, context: ActorContext, path: String): Future[Unit] = Future.successful(context.actorSelection(path) ! a)

    def tellFrom(a: Any, to: ActorRef, from: ActorRef): Future[Unit] = Future.successful(to tell(a, from))

    def timeOut(context: ActorContext, time: Int): Future[Unit] = {
      if (time == 0) {
        Future.successful(context.setReceiveTimeout(Duration.Undefined))
      }
      else {
        Future.successful(context.setReceiveTimeout(time seconds))
      }
    }

    def transitionState(context: ActorContext, state: () => Receive): Future[Unit] = Future.successful(context.become(state()))

    def stopActor(context: ActorContext, victim: ActorRef): Future[Unit] = Future.successful(context.stop(victim))

    def createActor(props: Props, name: String, context: ActorContext): Future[ActorRef] = Future.successful(context.actorOf(props, name))

  }


  implicit val StatelessLoggerInterpreter = new LoggerOp.Handler[Future] {
    def debug(msg: String, o: List[Event]): Future[Unit] = Future.successful(println(s"[Debug] : ${msg}"))

    def info(msg: String, o: List[Event]): Future[Unit] = Future.successful(println(s"[Info] : ${msg}"))

    def warn(msg: String, o: List[Event]): Future[Unit] = Future.successful(println(s"[Warn] : ${msg}"))

    def error(msg: String, o: List[Event]): Future[Unit] = Future.successful(println(s"[Error] : ${msg}"))
  }

  implicit val DisplayOpInterpreter = new DisplayOp.Handler[Future] {
    def send(a: Any, to: ActorRef): Future[Unit] = Future.successful(println("send function::::" + a))
  }
}


object CombineInterpreters {
  //val loggerInt: LoggerOp.Handler[Id]
  //val actorInt: ActorOp.Handler[Id]
  //val displayInt: DisplayOp.Hanler[Id]
  import InterpretersId._

  //implicit val loggerAndActorInterpreter: ParInterpreter[FinalApp.Op ,Id] = FSHandler[]
  //implicit val finalApp: ParInterpreter[FinalApp.Op ,Id] = implicitly[ParInterpreter[FinalApp.Op ,Id]]
  //implicitly[FinalApp.Op ~> Id]
}

