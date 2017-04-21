import cats.data.Kleisli

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Interpreters {

  import Algebra._

  type ParValidator[A] = Kleisli[Future, String, A]


  implicit val interpreter = new Validation.Handler[ParValidator] {
    def minSize(n: Int): ParValidator[Boolean] =
      Kleisli { s =>
        Future {
          println("x before")
          Thread.sleep(4000)
          println("x after")
          s.size >= n
        }
      }

    def hasNumber: ParValidator[Boolean] =
      Kleisli { s =>
        Future {
          println("y before")
          Thread.sleep(100)
          println("y after")
          s.exists(c => "0123456789".contains(c))
        }
      }
  }
}
