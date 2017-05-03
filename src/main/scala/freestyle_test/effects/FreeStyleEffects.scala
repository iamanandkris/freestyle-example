package freestyle_test.effects

/**
  * Created by abdhesh on 10/04/17.
  */
object FreeStyleEffects extends App {

  import freestyle._
  import freestyle.implicits._
  import freestyle.effects.error._
  import freestyle.effects.error.implicits._
  import cats.implicits._

  val boom = new RuntimeException("BOOM")

  type Target[A] = Either[Throwable, A]

  def shortCircuit[F[_] : ErrorM] =
    for {
      a <- FreeS.pure(1)
      b <- ErrorM[F].either[Int](Left(boom))
      c <- FreeS.pure(1)
    } yield a + b + c

  def shortCircuitWithError[F[_] : ErrorM] =
    for {
      a <- FreeS.pure(1)
      b <- ErrorM[F].error[Int](boom)
      c <- FreeS.pure(1)
    } yield a + b + c

  def continueWithRightValue[F[_] : ErrorM] =
    for {
      a <- FreeS.pure(1)
      b <- ErrorM[F].either[Int](Right(1))
      c <- FreeS.pure(1)
    } yield a + b + c

  import cats.Eval

  def catchingExceptions[F[_] : ErrorM] =
    for {
      a <- FreeS.pure(1)
      b <- ErrorM[F].catchNonFatal[Int](Eval.later(throw new RuntimeException))
      c <- FreeS.pure(1)
    } yield a + b + c

  import freestyle.effects.option._
  import freestyle.effects.option.implicits._

  def programNone[F[_] : OptionM] =
    for {
      a <- FreeS.pure(1)
      b <- OptionM[F].option[Int](None)
      c <- FreeS.pure(1)
    } yield a + b + c

  def programSome[F[_] : OptionM] =
    for {
      a <- FreeS.pure(1)
      b <- OptionM[F].option(Some(1))
      c <- FreeS.pure(1)
    } yield a + b + c

  def programNone2[F[_] : OptionM] =
    for {
      a <- FreeS.pure(1)
      b <- OptionM[F].none[Int]
      c <- FreeS.pure(1)
    } yield a + b + c

  val resultLeft = shortCircuit[ErrorM.Op].interpret[Target]
  val resultRight = continueWithRightValue[ErrorM.Op].interpret[Target]
  println(resultLeft)
  println(resultRight)

  val catchException = catchingExceptions[ErrorM.Op].interpret[Target]
  println(catchException)
  val optResult = programNone[OptionM.Op].interpret[Option]
  println(optResult)

  val optResultSome = programSome[OptionM.Op].interpret[Option]
  println(optResultSome)

  val nonResult = programNone2[OptionM.Op].interpret[Option]
  println(nonResult)
}
