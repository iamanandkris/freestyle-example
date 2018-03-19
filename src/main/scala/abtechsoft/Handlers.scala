package abtechsoft

import cats.data.EitherT

import scala.concurrent.Future


object Handlers {

  import Programs._
  import scala.concurrent.ExecutionContext.Implicits.global
  import cats.implicits._

  //either handler
  implicit val someAction: SomeAction.Handler[Target] = new SomeAction.Handler[Target] {
    override protected[this] def action1(s: String): EitherT[Future, Outcomes, String] = {

      println("INTERPRETED -->>>>")
      //EitherT.right(Future("test0"))
      //EitherT.right(Future.failed(Faild))
      EitherT.right(Future {
        "test"
      })
      //val f = Future("test0")
    }

  }


  implicit val someotherAction: SomeOtherAction.Handler[Target] = new SomeOtherAction.Handler[Target] {

    override protected[this] def otheraction1(s: String): EitherT[Future, Outcomes, String] = {

      println("RESPONSE ----->>>")
      EitherT.rightT(s)
    }

    override protected[this] def otheraction2(s: String): EitherT[Future, Outcomes, String] = {

      println("INTERPRETED OTHER 2 -->>>> ")
      //Right(Future.successful("test"))
      val t = Future("")
      //EitherT.leftT[Future, String](KafkaNotAvailable)
      //EitherT.right(Future.failed(Faild))
      /* EitherT.right(Future{
         ""
       })*/
      EitherT.leftT("sasa")
    }


    override protected[this] def otheraction3(s: String): EitherT[Future, Outcomes, String] = {

      println("INTERPRETED OTHER 3 -->>>>")
      //Right("test")
      EitherT.leftT("dasas")
    }

  }
}