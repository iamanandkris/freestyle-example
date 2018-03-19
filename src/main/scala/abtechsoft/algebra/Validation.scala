package abtechsoft.algebra

import cats.data.ValidatedNel
import freestyle.free.free

@free
trait Validation {
  def mapFailures[E, X](validated: ValidatedNel[E, X]): FS[X]
}
