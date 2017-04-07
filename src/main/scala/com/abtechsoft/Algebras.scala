package com.abtechsoft

import freestyle._

/**
  * Created by abdhesh on 06/04/17.
  */
object Algebras {

  @free trait Database[F[_]] {
    def get(id: Int): FreeS[F, Option[User]]

    def save(user: Option[User]): FreeS[F, Option[User]]

    def getAll(filter: String): FreeS[F, List[User]]
  }

  @free trait Cache[F[_]] {
    def get(id: Int): FreeS[F, Option[Int]]
  }

  @free trait Presenter[F[_]] {
    def show(user: Option[User]): FreeS[F, Unit]
  }

  @free trait IdValidation[F[_]] {
    def validate(id: Option[Int]): FreeS[F, Int]
  }

  @free trait Arith[F[_]] {
    def add(x:Int,y:Int):FreeS[F,Int]
    def subtract(x:Int,y:Int):FreeS[F,Int]
  }

  @free trait SagaOP[F[_]] {
    def updateAccount(account:(String,String,Double)):FreeS[F,Boolean]
    def updateUser(user:(String,String,Int)):FreeS[F,Boolean]
  }

  @free trait DBOperation[F[_]] {
    def getUserById(key:String):FreeS.Par[F,(String,String,Int)]
    def getAccountById(key:String):FreeS.Par[F,(String,String,Double)]
  }

  @free trait ValidationOp[F[_]] {
    def validateUser(inp:(String,String,Int)):FreeS.Par[F,Boolean]
    def validateAccount(inp:(String,String,Double)):FreeS.Par[F,Boolean]
  }
}
