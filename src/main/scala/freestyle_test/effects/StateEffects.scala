package freestyle_test.effects

/**
  * Created by abdhesh on 10/04/17.
  */
object StateEffects extends App {

  import freestyle._
  import freestyle.implicits._
  import cats.implicits._
  import freestyle.effects.state
  import cats.data.State

  val st = state[Int]

  type TargetState[A] = State[Int, A]

  import st.implicits._

  def programGet[F[_] : st.StateM] =
    for {
      a <- FreeS.pure(1)
      b <- st.StateM[F].get
      c <- FreeS.pure(1)
    } yield a + b + c

  def programSet[F[_] : st.StateM] =
    for {
      _ <- st.StateM[F].set(1)
      a <- st.StateM[F].get
    } yield a


  val stateResult = programGet[st.StateM.Op].exec[TargetState].run(1).value
  val stateResultSet = programSet[st.StateM.Op].exec[TargetState].run(0).value
  println(stateResultSet)
  println(stateResult)
}
