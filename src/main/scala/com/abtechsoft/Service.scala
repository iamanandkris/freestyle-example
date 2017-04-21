package com.abtechsoft

import com.abtechsoft.Modules._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import Interpreters._
import cats.implicits._
import freestyle._
import freestyle.implicits._

/**
  * Created by abdhesh on 06/04/17.
  */
object Service {
  def getUser(): Future[Unit] = Programs.program(1).exec[Future]
}
