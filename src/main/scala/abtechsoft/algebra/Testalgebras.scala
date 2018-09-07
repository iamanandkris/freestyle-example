package abtechsoft.algebra
import freestyle.free._
import freestyle.free.implicits
object Testalgebras {

  @free trait Database {
    def get(id: Int): FS[Int]
  }

  @free trait Cache {
    def get(id: Int): FS[Option[Int]]
  }

  @free trait Presenter {
    def show(id: Int): FS[Int]
  }

  @free trait IdValidation {
    def validate(id: Option[Int]): FS[Int]
  }

}
