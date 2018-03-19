package abtechsoft

import abtechsoft.Programs.{SomeAction, SomeOtherAction}
import cats.data.EitherT
import cats.implicits._
import freestyle.free._
import freestyle.free.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Programs {

  case class KafkaNotAvailable(message: String = "")

  @free trait SomeAction {
    def action1(s: String): FS[String]
  }


  @free trait SomeOtherAction {
    def otheraction1(s: String): FS[String]

    def otheraction2(s: String): FS[String]

    def otheraction3(s: String): FS[String]
  }

  type Outcomes = String
  //type Target[A] = Either[Outcomes, A]
  type Target[A] = EitherT[Future, Outcomes, A]
  //type Target[A] = Future[A]


  //pgm1
  def act()(implicit etr: ActionModule[ActionModule.Op]): FreeS[ActionModule.Op, String] = {

    for {
      b <- etr.actions.action1("")
      //d <- smallerpgm() //http for batch and materializer pgms
      c <- etr.otheraction.otheraction2(b)
      e <- etr.otheraction.otheraction1("")
    } yield c
  }

  def smallerpgm()(implicit etr: ActionModule[ActionModule.Op]): FreeS[ActionModule.Op, String] = {
    for {
      a <- etr.otheraction.otheraction2("")
      b <- etr.otheraction.otheraction3(a)
    } yield ""
  }


}


@module trait ActionModule {
  val actions: SomeAction
  val otheraction: SomeOtherAction
}

