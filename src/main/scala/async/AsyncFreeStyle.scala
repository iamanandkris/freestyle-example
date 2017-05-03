package async

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by abdhesh on 18/04/17.
  */
object AsyncFreeStyle extends App {

  import freestyle._
  import freestyle.implicits._
  import freestyle.async._
  import freestyle.async.implicits._

  def sortedBooks[F[_] : AsyncM](lib: LibraryAPI)(author: Author): FreeS[F, List[Book]] =
    AsyncM[F].async[List[Book]] { cb =>
      lib.findBooks(author)({ books => cb(Right(books)) }, { error => cb(Left(error)) })
    }

  val getSorted: Author => FreeS[AsyncM.Op, List[Book]] =
    sortedBooks[AsyncM.Op](Library) _

  val dickensBooks = getSorted(Author("Dickens"))
  val otherBooks = getSorted(Author("HemingwayOrwellKerouac"))

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  import cats.implicits._

  //val fut1 = dickensBooks.exec[Future]
  val fut2 = otherBooks.interpret[Future]
 // println(Await.result(fut1, Duration.Inf))
  println(Await.result(fut2, Duration.Inf))

}

case class Author(name: String)

case class Book(name: String, id: Int)

case class NoBooksFound(author: Author) extends Exception(s"No books found from ${author.name}")

trait LibraryAPI {
  def findBooks(author: Author)(withBooks: List[Book] => Unit, error: Throwable => Unit): Unit
}

object Library extends LibraryAPI {
  def findBooks(author: Author)(withBooks: List[Book] => Unit, error: Throwable => Unit): Unit =
    if (author.name.toLowerCase == "dickens") error(NoBooksFound(author))
    else withBooks(Book("The Old Man and the Sea", 1951) :: Book("1984", 1948) :: Book("On the Road", 1957) :: Nil)
}