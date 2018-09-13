package abtechsoft.programs

import abtechsoft.algebra.TestModules.InternalInterfaceModule
import freestyle.free._

trait TestWrapperBehaviour[T <:TestDataWrapper] {
  def getDataExec(data:T):FreeS[InternalInterfaceModule.Op, Int]
}

object TestWrapperBehaviour{
  import StandaloneBehaviour._
  import TestBehaviour._

  implicit object ABCTestWrapperBehaviour extends TestWrapperBehaviour[ABCWrapper] {
    override def getDataExec(data: ABCWrapper):FreeS[InternalInterfaceModule.Op, Int] ={
      ABCTestBehaviour.behaviour(data.value)(data.module.display)
    }
  }
}