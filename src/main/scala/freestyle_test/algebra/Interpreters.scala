package freestyle_test.algebra

import scala.concurrent.Future


/**
  * Created by abdhesh on 12/04/17.
  */
object Interpreters {

  implicit val parserInterpreter = new Algebra.Parser.Handler[Future] {
    def parse(text: String): Future[Option[(List[String], String)]] = {
      Future.successful(Some(text.split(",").toList -> "Un-parsed"))
    }
  }
}
