package abtechsoft.programs

import abtechsoft.algebra.TestModules
import abtechsoft.algebra.TestModules.Display
import abtechsoft.algebra.Testalgebras.{Cache, Database, IdValidation, Presenter}
import freestyle.free._
import freestyle.free.implicits
import freestyle.free.internal.EffectLike

trait TestBehaviour[T <:TestDataType] {
  def behaviour[F[_] <: _root_.iota.CopK[_,_]](value:T)(implicit app1: Display[F]): FreeS[F, Int]
}

object TestBehaviour{
  implicit object ABCTestBehaviour extends TestBehaviour[ABC]{
    override def behaviour[F[_]  <: _root_.iota.CopK[_,_]](value: ABC)(implicit app1: Display[F]): FreeS[F, Int] ={
      for {
        id   <- app1.validator.validate(Some(value.v.length))
        view <- app1.presenter.show(id)
      } yield view
    }
  }
}
