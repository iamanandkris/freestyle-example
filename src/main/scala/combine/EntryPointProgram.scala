package combine

import akka.actor.ActorRef
import cats.Id
import cats.implicits._
import combine.Algebras.DisplayOp
import freestyle._
import freestyle.implicits._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by abdhesh on 27/04/17.
  */
object EntryPointProgram extends App {
  import InterpretersId._
  val a = Programs.M3Programs.logSendAndDisplay(ActorRef.noSender, "string").exec[Id]
}
