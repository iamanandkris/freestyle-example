package abtechsoft

import abtechsoft.Programs.Target

object FSEitherM extends App {

  import Handlers._
  import freestyle.free._
  import freestyle.free.implicits._
  import cats.implicits._
  import cats.data.EitherT._
  import scala.concurrent.ExecutionContext.Implicits.global

  val tt = Programs.act().interpret[Target]

  println(tt)

}