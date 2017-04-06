package com.abtechsoft
import scala.concurrent.Await
import scala.concurrent.duration._
/**
  * Created by abdhesh on 06/04/17.
  */
object EntryPoint extends App {
  val result = Service.getUser()
  Await.result(result,Duration(100, MILLISECONDS))
  println(result)
  Thread.sleep(200)

}
