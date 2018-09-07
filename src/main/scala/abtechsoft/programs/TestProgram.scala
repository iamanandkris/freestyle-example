package abtechsoft.programs

import freestyle.free._
object TestProgram {
  import abtechsoft.algebra.TestModules._
  import TestBehaviour._

  def foo[A<:TestDataType](thing: A)(implicit evidence: TestBehaviour[A], app: App[App.Op]) = evidence.behaviour(thing)(app.display)

  def anotherProgram(implicit app: App[App.Op]): FreeS[App.Op, Int] = {
    for{
      value       <- foo(ABC("thisisatest"))
      cachedToken <- app.persistence.cache.get(value + 1)
      id          <- app.display.validator.validate(cachedToken)
      value       <- app.persistence.database.get(id)
      view        <- app.display.presenter.show(value)
    }yield view
  }
}
