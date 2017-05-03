package kstore

/**
  * Created by abdhesh on 06/04/17.
  */
object Module {

  import Algebras._
  import freestyle._

  @module trait Backend {
    val store: KVStore
    val log: Log
  }
}