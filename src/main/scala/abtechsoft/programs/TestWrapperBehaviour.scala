package abtechsoft.programs

import freestyle.free._

trait TestWrapperBehaviour[T <:TestDataWrapper] {
  def getDataExec(data:T):FreeS[_root_.iota.CopK[?,_], Int]
}

object TestWrapperBehaviour{
  import StandaloneBehaviour._
  import TestBehaviour._

  implicit object ABCTestWrapperBehaviour extends TestWrapperBehaviour[ABCWrapper] {
    override def getDataExec(data: ABCWrapper):FreeS[_root_.iota.CopK[?, _], Int] ={
      val kkk = ABCTestBehaviour.behaviour(data.value)(data.module.display)
      1234
      kkk
    }
  }
}