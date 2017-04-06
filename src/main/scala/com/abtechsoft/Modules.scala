package com.abtechsoft

import Algebras._
import freestyle._

/**
  * Created by abdhesh on 06/04/17.
  */
object Modules {

  @module trait Persistence[F[_]] {
    val database: Database[F]
    val cache: Cache[F]
  }

  @module trait Display[F[_]] {
    val presenter: Presenter[F]
    val validator: IdValidation[F]
  }

  @module trait App[F[_]] {
    val persistence: Persistence[F]
    val display: Display[F]
  }

}
