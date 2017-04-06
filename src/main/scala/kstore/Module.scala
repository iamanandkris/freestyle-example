package kstore

/**
  * Created by abdhesh on 06/04/17.
  */
object Module {

  import Algebras._
  import freestyle._

  @module trait Backend[F[_]] {
    val store: KVStore[F]
    val log: Log[F]
  }
}