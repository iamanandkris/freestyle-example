package combine

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
object EntryPoint extends App {

  //import CombineInterpreters._
  import InterpretersId._
  val a = Programs.program.interpret[Id]
  //Await.result(a, 1 second)
}
