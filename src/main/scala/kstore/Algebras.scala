package kstore

import cats.free.FreeApplicative


/**
  * Created by abdhesh on 06/04/17.
  */
object Algebras {

  import freestyle._
  import freestyle.implicits._
  import cats.implicits._

  @free trait KVStore {
    def put[A](key: String, value: A): FS[Unit]

    def get[A](key: String): FS[Option[A]]

    def delete(key: String): FS[Unit]

    def update[A](key: String, f: A => A): FS.Seq[Unit] =
      get[A](key).freeS flatMap {
        case Some(a) => put[A](key, f(a)).freeS
        case None => ().pure[FS.Seq]
      }

  }

  @free trait Log {
    def info(msg: String): FS[Unit]

    def warn(msg: String): FS[Unit]
  }

}
