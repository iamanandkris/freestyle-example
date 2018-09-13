package abtechsoft.programs

import abtechsoft.algebra.TestModules.InternalInterfaceModule

trait TestDataType
case class ABC(v:String) extends TestDataType
case class EFG(v:Double) extends TestDataType


trait TestDataWrapper
case class ABCWrapper(value:ABC, module:InternalInterfaceModule[InternalInterfaceModule.Op]) extends TestDataWrapper
case class EFGWrapper(value:EFG, module:InternalInterfaceModule[InternalInterfaceModule.Op]) extends TestDataWrapper