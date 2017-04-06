package kstore

/**
  * Created by abdhesh on 06/04/17.
  */
object Programs {

  import Module._
  import freestyle._

  def program[F[_]](implicit B: Backend[F]): FreeS[F, Option[Int]] = {
    import B.store._, B.log._
    for {
      _ <- put("wild-cats", 2)
      _ <- info("Added wild-cats")
      _ <- update[Int]("wild-cats", (_ + 12))
      _ <- info("Updated wild-cats")
      _ <- put("tame-cats", 5)
      n <- get[Int]("wild-cats")
      _ <- delete("tame-cats")
      _ <- warn("Deleted tame-cats")
    } yield n
  }
}
