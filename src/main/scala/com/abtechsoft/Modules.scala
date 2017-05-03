package com.abtechsoft

import Algebras._
import freestyle._

/**
  * Created by abdhesh on 06/04/17.
  */
object Modules {

  @module trait Persistence {
    val database: Database
    val cache: Cache
  }

  @module trait Display {
    val presenter: Presenter
    val validator: IdValidation
  }

  @module trait App {
    val persistence: Persistence
    val display: Display
  }

  @module trait App1 {
    val persistence: Persistence
  }

  @module trait AccountUpdateServiceApp {
    val validation: ValidationOp
    val dbOperation: DBOperation
    val arithOperation: Arith
    val sagaOperation: SagaOP
  }

}



