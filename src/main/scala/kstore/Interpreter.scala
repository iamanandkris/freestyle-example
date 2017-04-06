package kstore

/**
  * Created by abdhesh on 06/04/17.
  */
object Interpreter {

  import Algebras._
  import cats.implicits._
  import cats.data.State

  // import cats.data.State
  type KVStoreState[A] = State[Map[String, Any], A]
  // defined type alias KVStoreState
  implicit val kvStoreHandler: KVStore.Handler[KVStoreState] = new KVStore.Handler[KVStoreState] {
    def put[A](key: String, value: A): KVStoreState[Unit] =
      State.modify(_.updated(key, value))

    def get[A](key: String): KVStoreState[Option[A]] =
      State.inspect(_.get(key).map(_.asInstanceOf[A]))

    def delete(key: String): KVStoreState[Unit] =
      State.modify(_ - key)
  }

  implicit def logHandler: Log.Handler[KVStoreState] = new Log.Handler[KVStoreState] {
    def info(msg: String): KVStoreState[Unit] = println(s"INFO: $msg").pure[KVStoreState]

    def warn(msg: String): KVStoreState[Unit] = println(s"WARN: $msg").pure[KVStoreState]
  }
}

/*object Alternate {

  import cats.~>
  import cats.data.State

  type KVStoreState[A] = State[Map[String, Any], A]

  implicit def manualKvStoreHandler: KVStore.Op ~> KVStoreState = new (KVStore.Op ~> KVStoreState) {
    def apply[A](fa: KVStore.Op[A]): KVStoreState[A] =
      fa match {
        case KVStore.PutOP(key, value) =>
          State.modify(_.updated(key, value))
        case KVStore.GetOP(key) =>
          State.inspect(_.get(key).map(_.asInstanceOf[A]))
        case KVStore.DeleteOP(key) =>
          State.modify(_ - key)
      }
  }
}*/
