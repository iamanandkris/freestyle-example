package kstore



/**
  * Created by abdhesh on 06/04/17.
  */
object Algebras {

  import freestyle._
  import cats.implicits._

  @free trait KVStore[F[_]] {
    def put[A](key: String, value: A): FreeS[F, Unit]

    def get[A](key: String): FreeS[F, Option[A]]

    def delete(key: String): FreeS[F, Unit]

    def update[A](key: String, f: A => A): FreeS[F, Unit] =
      get[A](key) flatMap {
        case Some(a) => put[A](key, f(a))
        case None => FreeS.pure()
      }
  }

  @free trait Log[F[_]] {
    def info(msg: String): FreeS[F, Unit]

    def warn(msg: String): FreeS[F, Unit]
  }

}
