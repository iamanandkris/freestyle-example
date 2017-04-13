package freestyle

import akka.actor.ActorSystem
import freestyle.FSCallSupervisor.Message

/**
  * Created by abdhesh on 12/04/17.
  */
object EntryPoint extends App {

  val system = ActorSystem("call-system")
  val sCallSupervisor = system.actorOf(FSCallSupervisor.props, FSCallSupervisor.name)
  sCallSupervisor ! Message("132,55000.0,John Walker")
}
