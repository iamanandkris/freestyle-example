import freestyle.{FreeS, free}

/**
  * Created by abdhesh on 13/04/17.
  */
object Algebra {
  @free
  trait Validation[F[_]] {
    def minSize(n: Int): FreeS.Par[F, Boolean]

    def hasNumber: FreeS.Par[F, Boolean]
  }
}
