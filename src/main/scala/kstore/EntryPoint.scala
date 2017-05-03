package kstore

/**
  * Created by abdhesh on 06/04/17.
  */
object EntryPoint extends App {

  import freestyle.implicits._
  import freestyle._
  import Interpreter._
  import Module._

  val result = Programs.program[Backend.Op].interpret[KVStoreState]
  println(result.run(Map.empty[String,Any]).value._1)
}
