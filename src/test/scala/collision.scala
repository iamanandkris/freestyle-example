import cats.data.{NonEmptyList, Validated}
import freestyle.free._
import freestyle.free.effects._

object collision {
  val wr = writer[List[Int]]

  val st = state[Int]
  val rd = reader[Config]

  @module
  trait AppX {
    val stateM: st.StateM
    val readerM: rd.ReaderM
  }

  @free
  trait B {
    def x: FS[Int]
  }

  @free
  trait C {
    def x: FS[Int]
  }

  @free
  trait D {
    def x: FS[Int]
  }

  @free
  trait E {
    def x: FS[Int]
  }

  @free
  trait F {
    def validate: FS[Validated[NonEmptyList[Exception], String]]
  }

  @module
  trait X {
    val a: B
    val b: C
    val f: F

    def program = for {
      _ <- a.x
      _ <- b.x
      v <- f.validate
    } yield v
  }

  @module
  trait Y {
    val c: C
    val d: D
  }

  @module
  trait Z {
    val x: X
    val y: Y
  }

  case class Config(n: Int = 5)

}
