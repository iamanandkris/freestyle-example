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
  * Created by abdhesh on 10/04/17.
  */
object MixingSequentialTask extends  App {

  import freestyle._
  import freestyle.implicits._
  import cats.implicits._

  import TaskInterpreters._
  import com.abtechsoft.Modules._
  import TaskMonad._

  val resultTask = Programs.accountUpdateprogram[AccountUpdateServiceApp.Op](("new", "newDep", 34.45), ("new", "newDep", 34)).interpret[TaskInterpreters.ParMixedTask]

  val output = Await.result(resultTask().runAsync, 10 seconds)
  println(output)

}
