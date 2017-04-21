package com.abtechsoft

import com.abtechsoft.Modules._
import freestyle._
import freestyle._
import freestyle.implicits._
import cats.implicits._

/**
  * Created by abdhesh on 06/04/17.
  */
object Library{
  val app = App[App.Op]
}

object Programs {

  /**
    * DEPENDENCY INJECTION
    */
  import Library.app._
  def program(id: Int): FreeS[App.Op, Unit] = {
    import display._
    import persistence._
    for {
      cachedToken <- cache.get(1)
      id <- validator.validate(cachedToken)
      value <- database.get(id)
      _ <- presenter.show(value)
    } yield ()
  }


  def program1[F[_]](id: Int)(implicit app: App[F]): FreeS[F, Unit] = {
    import app.display._
    import app.persistence._
    for {
      cachedToken <- cache.get(1)
      id <- validator.validate(cachedToken)
      value <- database.get(id)
      _ <- presenter.show(value)
    } yield ()
  }

  def accountUpdateprogram[F[_]](account:(String,String,Double),user:(String,String,Int))(implicit app: AccountUpdateServiceApp[F]): FreeS[F, Boolean] = {
    import app.arithOperation._
    import app.dbOperation._
    import app.validation._
    import app.sagaOperation._

    for {
      validationResults     <- (validateUser(user) |@| validateAccount(account)).tupled.freeS
      (validUser, validAccount) = validationResults

      updatedUserAge        <- if (validUser) add(user._3, 1)  else subtract(user._3, 1)
      updatedAccountCredit  <- if (validAccount) FreeS.pure[F, Double](account._3 * 0.5) else FreeS.pure[F,Double](account._3)

      dbResults             <- (getUserById(user._1) |@| getAccountById(account._1)).tupled.freeS
      (userOld, accountOld) = dbResults

      userUpdated           <- updateUser((userOld._1, userOld._2,updatedUserAge))
      accountUpdated        <- updateAccount((accountOld._1,accountOld._2,updatedAccountCredit))
    } yield userUpdated && accountUpdated
  }
}
