package abtechsoft.programs

trait TestDataType
case class ABC(v:String) extends TestDataType
case class EFG(v:Double) extends TestDataType


trait TestDataWrapper
case class ABCWrapper(value:ABC, module:abtechsoft.algebra.TestModules.App[abtechsoft.algebra.TestModules.App.Op]) extends TestDataWrapper
case class EFGWrapper(value:EFG, module:abtechsoft.algebra.TestModules.App[abtechsoft.algebra.TestModules.App.Op]) extends TestDataWrapper