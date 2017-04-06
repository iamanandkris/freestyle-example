package com.abtechsoft

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.abtechsoft.Algebras._
/**
  * Created by abdhesh on 06/04/17.
  */
object Interpreters {
  implicit val userHandler = new Database.Handler[Future] {
    val users = mutable.ListBuffer(User(1, "user1"), User(2, "user2"), User(3, "user3"))

    def get(id: Int): Future[Option[User]] = Future.successful(users.find(_.id == id))

    def save(user: Option[User]): Future[Option[User]] = Future {
      user.foreach { u => users += u }
      user
    }

    def getAll(filter: String): Future[List[User]] = Future.successful(users.find(_.name.contains(filter)).toList)
  }
  implicit val cache = new Cache.Handler[Future] {
    val m = mutable.Map[Int, Int](1 -> 1, 2 -> 2, 1 -> 3)

    def get(id: Int): Future[Option[Int]] = Future.successful(m.get(id))
  }
  implicit val presenter = new Presenter.Handler[Future] {
    def show(user: Option[User]): Future[Unit] = Future {
      println(user)
    }
  }
  implicit val idValidation = new IdValidation.Handler[Future] {
    def validate(id: Option[Int]): Future[Int] = Future.successful(id.getOrElse(0))
  }
}
