package nondeterminism

import cats.data.Kleisli
import com.abtechsoft.{Interpreters, Programs}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

/**
  * Created by abdhesh on 06/04/17.
  */
object MixingSequential extends App {

  import freestyle._
  import freestyle.implicits._
  import cats.implicits._

  /*@free trait MixedFreeS[F[_]] {
    def validationOp: FreeS.Par[F, Int]

    def databaseOp: FreeS.Par[F, Double]

    def sagaOp(intVal:Int,dVal:Double): FreeS[F, String]
  }

  import freestyle.implicits._
  import cats.implicits._

  type ParMixed[A] = Kleisli[Future, Unit, A]

  implicit val mixed = new MixedFreeS.Handler[ParMixed] {
    def validationOp: ParMixed[Int] = Kleisli(s => Future {
      println("future validationOp::" + s)
      //Thread.sleep(200)
      2
    })

    def databaseOp: ParMixed[Double] = Kleisli(s => Future {
      println("future databaseOp ::" + s)
      Thread.sleep(2000)
      12.34
    })

    def sagaOp(intVal:Int,dVal:Double): ParMixed[String] = Kleisli(s => Future {
      println("future sagaOp ::" + s)
      (intVal+dVal).toString
    })

  }

  def programWeird[F[_]](implicit M: MixedFreeS[F]) = {
    import M._
    for {
      bc <- (validationOp |@| databaseOp).tupled.freeS
      (b, c) = bc
      d <- sagaOp(b,c) //3
      bcp <- (validationOp |@| databaseOp).tupled.freeS
      (bb, cc) = bcp
      dk <- sagaOp(bb,cc) //3
    } yield d+dk
  }
*/

  import Interpreters._
  import com.abtechsoft.Modules._

  val result = Programs.accountUpdateprogram[AccountUpdateServiceApp.Op](("new","newDep", 34.45),("new","newDep", 34)).exec[Interpreters.ParMixedFuture]

  val output = Await.result(result(), 10 seconds)
  println(output)
}
