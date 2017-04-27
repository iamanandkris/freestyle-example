package freestyle_test

import akka.actor.{Actor, ActorRef}
import akka.util.ByteString
import akka.pattern.pipe
import cats.implicits._
import freestyle_test.FSCallSupervisor.ParsedMessage
import freestyle._
import freestyle.implicits._
import freestyle_test.algebra.Interpreters._

import scala.concurrent.Future

/**
  * Created by abdhesh on 12/04/17.
  */
trait FreeSwitchActor extends Actor {
  self: FreeSwitch =>
  var buffer = ""

  implicit val ec = context.dispatcher

  override def receive: Receive = {
    case in: ByteString =>
      val result: Future[Option[(List[String], String)]] = parseMessage[freestyle_test.algebra.Algebra.Parser.Op](buffer + in.utf8String).exec[Future]

      result.collect {
        case Some((list, _)) => ParsedMessage(list)
      } pipeTo sender()
    case ref: ActorRef =>
  }

}
