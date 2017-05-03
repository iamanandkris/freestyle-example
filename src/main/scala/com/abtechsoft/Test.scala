package com.abtechsoft

import freestyle.{FreeS, free}
import scala.collection.mutable
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import cats.implicits._
import freestyle._
import freestyle.implicits._

case class User(id: Long, name: String)

/**
  * Created by abdhesh on 06/04/17.
  */
object Test extends App {

  @free trait UserRepository {
    def get(id: Long): FS[Option[User]]

    def save(user: Option[User]): FS[Option[User]]

    def getAll(filter: String): FS[List[User]]
  }

  implicit val userHandler = new UserRepository.Handler[Future] {
    val users = mutable.ListBuffer(User(1, "user1"), User(2, "user2"), User(3, "user3"))

    def get(id: Long): Future[Option[User]] = Future.successful(users.find(_.id == id))

    def save(user: Option[User]): Future[Option[User]] = Future {
      user.foreach { u => users += u }
      user
    }

    def getAll(filter: String): Future[List[User]] = Future.successful(users.find(_.name.contains(filter)).toList)
  }

  def program[F[_]](implicit A: UserRepository[F]) = {
    import A._
    for {
      userInput <- get(1)
      _ <- save(userInput)
      users <- getAll("user1")
    } yield (users)
  }

  val futureValue = program[UserRepository.Op].interpret[Future]
  val result = Await.result(futureValue, Duration.Inf)
  println(futureValue)
}
