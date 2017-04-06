package com.abtechsoft

import com.abtechsoft.Modules._
import freestyle._

/**
  * Created by abdhesh on 06/04/17.
  */
object Programs {

  def program[F[_]](id: Int)(implicit app: App[F]): FreeS[F, Unit] = {
    import app.display._
    import app.persistence._
    for {
      cachedToken <- cache.get(1)
      id <- validator.validate(cachedToken)
      value <- database.get(id)
      _ <- presenter.show(value)
    } yield ()
  }
}
