package com.abtechsoft

import freestyle._

/**
  * Created by abdhesh on 06/04/17.
  */
object Algebras {

  @free trait Database[F[_]] {
    def get(id: Int): FreeS[F, Option[User]]

    def save(user: Option[User]): FreeS[F, Option[User]]

    def getAll(filter: String): FreeS[F, List[User]]
  }

  @free trait Cache[F[_]] {
    def get(id: Int): FreeS[F, Option[Int]]
  }

  @free trait Presenter[F[_]] {
    def show(user: Option[User]): FreeS[F, Unit]
  }

  @free trait IdValidation[F[_]] {
    def validate(id: Option[Int]): FreeS[F, Int]
  }

}
