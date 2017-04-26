/**
  * Created by abdhesh on 25/04/17.
  */

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActorRef, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

case class MyClass(str: String)

class MyActor extends Actor {
  override def receive: Receive = {
    case str: String => self ! (sender(), MyClass(str))
    case (a: ActorRef, MyClass(aa)) =>
      a ! aa
      println(aa)
  }
}

class MySpec() extends TestKit(ActorSystem("MySpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An Echo actor" must {

    "send back messages unchanged" in {
      val actorRef = TestActorRef[MyActor]
      val ms = "hello world"
      actorRef ! "hello world"
      expectMsg("hello world")
    }

  }
}