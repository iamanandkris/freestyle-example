package freestyle_test

import akka.actor.{Actor, Props}
import akka.util.ByteString
import freestyle_test.FSCallSupervisor.{Message, ParsedMessage}

/**
  * Created by abdhesh on 12/04/17.
  */
class FSCallSupervisor extends Actor {
  val callActor = context.actorOf(FSCall.props, FSCall.name)

  override def receive: Receive = {
    case Message(message) => callActor ! ByteString(message)
    case other =>
      println("Parsed Message::" + other)
  }
}

object FSCallSupervisor {
  def props: Props = Props(new FSCallSupervisor)

  def name: String = "call-supervisor"

  case class Message(message: String)

  case class ParsedMessage(messages: List[String])

}