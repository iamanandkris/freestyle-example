import freestyle.free.free
import org.scalatest.{FlatSpec, Matchers}
import freestyle.free._
import freestyle.free.implicits._
import cats.implicits._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ParallelTest extends FlatSpec with Matchers with ScalaFutures {

  @free trait MixedFreeS {
    def x: FS[Int]

    def y: FS[Int]

    def z: FS[Int]

    def program: FS.Seq[List[Int]] = {
      for {
        a <- z
        bc <- (x, y).tupled.freeS
        //(b, c) = bc
        d <- z
      } yield List(a, bc._1, bc._2, d)
    }
  }

  implicit val handler = new MixedFreeS.Handler[Future] {
    def x: Future[Int] = Future.successful(1)

    def y: Future[Int] = Future.successful(2)

    def z: Future[Int] = Future.successful(3)
  }

  it should "free instructions in parallel" in {
    val result = MixedFreeS[MixedFreeS.Op].program.interpret[Future]
    whenReady(result) {
      case list => println(list)
    }

  }
}
