package freestyle_test.effects

import cats.Id


/**
  * Created by abdhesh on 10/04/17.
  */
object ReaderEffects extends App {

  import freestyle._
  import freestyle.implicits._
  import freestyle.effects.reader
  import cats.data.Reader

  case class Config(n: Int)

  type ConfigEnv[A] = Reader[Config, A]

  val rd = reader[Config]

  import rd.implicits._

  def programAsk[F[_] : rd.ReaderM] =
    for {
      _ <- FreeS.pure(1)
      c <- rd.ReaderM[F].ask
      _ <- FreeS.pure(1)
    } yield c

  def programReader[F[_] : rd.ReaderM] =
    for {
      a <- FreeS.pure(1)
      b <- rd.ReaderM[F].reader(_.n)
      c <- FreeS.pure(1)
    } yield a + b + c

  val resultAsk: Id[Config] = programAsk[rd.ReaderM.Op].interpret[ConfigEnv].run(Config(n = 10))
  println(resultAsk)
  val resultReader: Id[Int] = programReader[rd.ReaderM.Op].interpret[ConfigEnv].run(Config(n = 1))
  println(resultReader)
}
