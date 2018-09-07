package abtechsoft.algebra

import freestyle.free.free

@free trait MemoryAlgebra {
  def save(value:String):FS[Unit]
}
