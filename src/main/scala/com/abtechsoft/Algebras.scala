package com.abtechsoft

import freestyle._
import freestyle.implicits._

/**
  * Created by abdhesh on 06/04/17.
  */
object Algebras {

  @free trait Database {
    def get(id: Int): FS[Option[User]]

    def save(user: Option[User]): FS[Option[User]]

    def getAll(filter: String): FS[List[User]]
  }

  @free trait Cache {
    def get(id: Int): FS[Option[Int]]
  }

  @free trait Presenter {
    def show(user: Option[User]): FS[Unit]
  }

  @free trait IdValidation {
    def validate(id: Option[Int]): FS[Int]
  }

  @free trait Arith {
    def add(x: Int, y: Int): FS[Int]

    def subtract(x: Int, y: Int): FS[Int]
  }

  @free trait SagaOP {
    def updateAccount(account: (String, String, Double)): FS[Boolean]

    def updateUser(user: (String, String, Int)): FS[Boolean]
  }

  @free trait DBOperation {
    def getUserById(key: String): FS[(String, String, Int)]

    def getAccountById(key: String): FS[(String, String, Double)]
  }

  @free trait ValidationOp {
    def validateUser(inp: (String, String, Int)): FS[Boolean]

    def validateAccount(inp: (String, String, Double)): FS[Boolean]
  }

}
