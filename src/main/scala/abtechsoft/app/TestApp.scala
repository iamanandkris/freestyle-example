package abtechsoft.app

import abtechsoft.programs.TestProgram
import freestyle.free._
import freestyle.free.implicits._
import cats.implicits

object TestApp extends App{
  import abtechsoft.handlers.TestHandlers._

  println(s"Received value - ${TestProgram.anotherProgram.interpret}")
}
