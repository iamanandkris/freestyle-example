package abtechsoft.algebra

import freestyle.free.free

@free trait LoggerAlgebra {
  def print(value:String):FS[Unit]
}
