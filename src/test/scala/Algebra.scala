import freestyle._

/**
  * Created by abdhesh on 13/04/17.
  */
object Algebra {
  @free
  trait Validation {
    def minSize(n: Int): FS[Boolean]
    def hasNumber: FS[Boolean]
  }
}
