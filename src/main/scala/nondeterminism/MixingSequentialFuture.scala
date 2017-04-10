package nondeterminism

import cats.data.Kleisli
import com.abtechsoft.{Interpreters, Programs, TaskInterpreters}
import monix.eval.Task
import monix.execution.CancelableFuture
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.{Await, Future}
//import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

/**
  * Created by abdhesh on 06/04/17.
  */
object MixingSequentialFuture extends App {

  import freestyle._
  import freestyle.implicits._
  import cats.implicits._

  import Interpreters._
  import com.abtechsoft.Modules._

  val result = Programs.accountUpdateprogram[AccountUpdateServiceApp.Op](("new","newDep", 34.45),("new","newDep", 34)).exec[Interpreters.ParMixedFuture]

  val output = Await.result(result(), 10 seconds)
  println(output)
}
