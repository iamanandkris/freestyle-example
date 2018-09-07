package abtechsoft.programs

import abtechsoft.algebra.TestModules.Display
import freestyle.free._

trait TestBehaviour[T <:TestDataType] {
  def behaviour[F[_]](value:T)(implicit app: Display[F]): FreeS[F, Int]
}

object TestBehaviour{
  implicit object ABCTestBehaviour extends TestBehaviour[ABC]{
    override def behaviour[F[_]](value: ABC)(implicit app: Display[F]): FreeS[F, Int] ={
      import app._
      for {
        id   <- validator.validate(Some(value.v.length))
        view <- presenter.show(id)
      } yield view
    }
  }
}
