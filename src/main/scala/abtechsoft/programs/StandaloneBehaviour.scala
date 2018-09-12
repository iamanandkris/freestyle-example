package abtechsoft.programs

import abtechsoft.algebra.TestModules.Display
import freestyle.free.FreeS

object StandaloneBehaviour {
  def foo[A<:TestDataType, F[_] <: _root_.iota.CopK[_,_]](thing: A, evidence: TestBehaviour[A])(implicit app: Display[F]):FreeS[F, Int] = evidence.behaviour[F](thing)
}
