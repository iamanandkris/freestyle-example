package abtechsoft.handlers

import cats.Id
import freestyle.free._
import freestyle.free.implicits

object TestHandlers {
  implicit val databaseHandler: abtechsoft.algebra.Testalgebras.Database.Handler[Id] =
    new abtechsoft.algebra.Testalgebras.Database.Handler[Id] {
      def get(id: Int): Id[Int] = id
    }
  implicit val cacheHandler: abtechsoft.algebra.Testalgebras.Cache.Handler[Id] =
    new abtechsoft.algebra.Testalgebras.Cache.Handler[Id] {
      def get(id: Int): Id[Option[Int]] = Some(id)
    }

  implicit val presenterHandler: abtechsoft.algebra.Testalgebras.Presenter.Handler[Id] =
    new abtechsoft.algebra.Testalgebras.Presenter.Handler[Id] {
      def show(id: Int): Id[Int] = id
    }

  implicit val idValHandler: abtechsoft.algebra.Testalgebras.IdValidation.Handler[Id] =
    new abtechsoft.algebra.Testalgebras.IdValidation.Handler[Id] {
      def validate(id: Option[Int]): Id[Int] = id match{
        case Some(value) => value
        case None => 0
      }
    }
}
