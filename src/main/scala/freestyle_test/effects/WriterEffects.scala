package freestyle_test.effects

/**
  * Created by abdhesh on 10/04/17.
  */
object WriterEffects extends App {

  import freestyle._
  import freestyle.implicits._
  import cats.implicits._
  import freestyle.effects.writer
  import cats.data.Writer

  val wr = writer[List[Int]]

  import wr.implicits._

  type Logger[A] = Writer[List[Int], A]

  def programWriter[F[_] : wr.WriterM] =
    for {
      _ <- FreeS.pure(1)
      b <- wr.WriterM[F].writer((Nil, 1))
      _ <- FreeS.pure(1)
    } yield b

  def programTell[F[_] : wr.WriterM] =
    for {
      _ <- FreeS.pure(1)
      b <- wr.WriterM[F].writer((List(1), 1))
      c <- wr.WriterM[F].tell(List(1))
      _ <- FreeS.pure(1)
    } yield b


  val result = programWriter[wr.WriterM.Op].interpret[Logger].run
  val resultTell = programTell[wr.WriterM.Op].interpret[Logger].run

  println(result)
  println(resultTell)
}
