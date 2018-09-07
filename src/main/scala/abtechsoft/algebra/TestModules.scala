package abtechsoft.algebra
import Testalgebras._
import freestyle.free._

object TestModules {
  @module trait Persistence {
    val database: Database
    val cache: Cache
  }
  @module trait Display {
    val presenter: Presenter
    val validator: IdValidation
    val cache: Cache
    val database: Database
  }
  @module trait App {
    val persistence: Persistence
    val display: Display
  }
}
