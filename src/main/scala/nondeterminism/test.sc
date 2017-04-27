import cats.Applicative
import cats.instances.list._
import cats.instances.option._
import cats.syntax.traverse._


//import cats.implicits._
List(1, 2, 3).traverse[Option,String](i => Some(i.toString))