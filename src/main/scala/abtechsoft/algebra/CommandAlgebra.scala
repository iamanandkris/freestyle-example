package abtechsoft.algebra

import abtechsoft.programs.{Command, FindNumberResponse}
import freestyle.free.free

@free trait CommandAlgebra {
  def create(data: List[FindNumberResponse]): FS[Command]

  def validation(data: List[(String, Long)]): FS[Unit]
}
