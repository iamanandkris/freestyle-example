package com.abtechsoft

import cats.data.Kleisli
import monix.eval.Task
import com.abtechsoft.Algebras._
import nondeterminism.TaskMonad

/**
  * Created by abdhesh on 06/04/17.
  */
object TaskInterpreters {
  import nondeterminism.KleisliImplicits._
  import TaskMonad._
  type ParMixedTask[A] = Kleisli[Task, Unit, A]

  implicit val arithInterp = new Arith.Handler[ParMixedTask]{
    def add(x:Int,y:Int):ParMixedTask[Int] = x + y
    def subtract(x:Int,y:Int):ParMixedTask[Int] = x - y
  }

  implicit val sagaInterp = new SagaOP.Handler[ParMixedTask]{
    def updateAccount(account:(String,String,Double)):ParMixedTask[Boolean] = true
    def updateUser(user:(String,String,Int)):ParMixedTask[Boolean] = true
  }



  implicit val dbOpInterp = new DBOperation.Handler[ParMixedTask]{
    def getUserById(key:String):ParMixedTask[(String,String,Int)] = (s: Unit) => Task {
      println("getUserById ::" + s)
      //Thread.sleep(1000)
      ("OldUserName", "OldDepartment", 34)
    }

    def getAccountById(key:String):ParMixedTask[(String,String,Double)] = Task {
      println("getAccountById ::")
      Thread.sleep(2000)
      ("OldUserName", "OldDepartment", 12.34)
    }
  }


  implicit val validationInterp = new ValidationOp.Handler[ParMixedTask]{
    def validateUser(inp:(String,String,Int)):ParMixedTask[Boolean] = (s: Unit) => Task {
      println("validateUser before ::" + s)
      Thread.sleep(4000)
      println("validateUser after ::" + s)
      true
    }

    def validateAccount(inp:(String,String,Double)):ParMixedTask[Boolean] = (s: Unit) => Task {
      println("validateAccount ::" + s)
      Thread.sleep(1000)
      true
    }
  }
}
