package com.abtechsoft

import cats.data.Kleisli
import monix.eval.Task

import com.abtechsoft.Algebras._

/**
  * Created by abdhesh on 06/04/17.
  */
object TaskInterpreters {
  type ParMixedTask[A] = Kleisli[Task, Unit, A]

  implicit val arithInterp = new Arith.Handler[ParMixedTask]{
    def add(x:Int,y:Int):ParMixedTask[Int] = Kleisli(s =>Task.pure(x+y))
    def subtract(x:Int,y:Int):ParMixedTask[Int] = Kleisli(s =>Task.pure(x - y))
  }

  implicit val sagaInterp = new SagaOP.Handler[ParMixedTask]{
    def updateAccount(account:(String,String,Double)):ParMixedTask[Boolean] = Kleisli(s =>Task.pure(true))
    def updateUser(user:(String,String,Int)):ParMixedTask[Boolean] = Kleisli(s =>Task.pure(true))
  }



  implicit val dbOpInterp = new DBOperation.Handler[ParMixedTask]{
    def getUserById(key:String):ParMixedTask[(String,String,Int)] = Kleisli(s => Task {
      println("getUserById ::" + s)
      //Thread.sleep(1000)
      ("OldUserName", "OldDepartment", 34)
    })
    def getAccountById(key:String):ParMixedTask[(String,String,Double)] = Kleisli(s => Task {
      println("getAccountById ::" + s)
      Thread.sleep(2000)
      ("OldUserName", "OldDepartment", 12.34)
    })
  }


  implicit val validationInterp = new ValidationOp.Handler[ParMixedTask]{
    def validateUser(inp:(String,String,Int)):ParMixedTask[Boolean] = Kleisli(s => Task {
      println("validateUser before ::" + s)
      Thread.sleep(4000)
      println("validateUser after ::" + s)
      true
    })
    def validateAccount(inp:(String,String,Double)):ParMixedTask[Boolean] = Kleisli(s => Task {
      println("validateAccount ::" + s)
      Thread.sleep(1000)
      true
    })
  }
}
