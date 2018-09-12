package abtechsoft.programs

import freestyle.free._


object TestProgram {
  import abtechsoft.algebra.TestModules._
  import TestBehaviour._
  import StandaloneBehaviour._

  def anotherProgram(implicit app: App[App.Op]): FreeS[App.Op, Int] = {

    val wrapper = ABCWrapper(ABC("thisisatest"), app)

    for{
      //cachedToken1 <- app.persistence.cache.get(1)
      value1       <- foo[ABC, App.Op](ABC("thisisatest"), ABCTestBehaviour)
      value        <- ABCTestBehaviour.behaviour(ABC("thisisatest"))(app.display)
      cachedToken  <- app.persistence.cache.get(1)
      id           <- app.display.validator.validate(cachedToken)
      value2       <- app.persistence.database.get(id)
      view         <- app.display.presenter.show(value)
    }yield view
  }
}
