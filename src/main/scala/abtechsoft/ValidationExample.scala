package abtechsoft

import freestyle.free.effects._
import cats.data.{State, StateT}
import cats.instances.future._
import cats.instances.list._
import cats.mtl.implicits._
import freestyle.free._
import freestyle.free.implicits._

import scala.concurrent.{ExecutionContext, Future}

object ValidationExample extends App {
  /*import freestyle.free.effects._
  import cats.data.Writer
  import cats.instances.list._
  import cats.mtl.implicits._

  import wr.implicits._

  type Logger[A] = Writer[List[Int], A]

  def program[F[_]: wr.WriterM] =
    for {
      _ <- FreeS.pure(1)
      b <- wr.WriterM[F].writer((List(1), 1))
      c <- wr.WriterM[F].tell(List(1))
      _ <- FreeS.pure(1)
    } yield b
  program[wr.WriterM.Op].interpret[Logger].run shouldBe Tuple2(List(1, 1), 1)
  println(result)*/
}
