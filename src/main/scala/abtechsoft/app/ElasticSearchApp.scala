package abtechsoft.app

import abtechsoft.programs.ElasticSearchQueryProgram
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object ElasticSearchApp extends ElasticClient {

  import freestyle.free._
  import freestyle.free.implicits._
  import cats.implicits._
  import cats.data.EitherT._
  import abtechsoft.handlers.ElasticQueryHandler._
  import abtechsoft.handlers.CommandAlgebraHandler._
  //import abtechsoft.handlers.ValidationHandler._
  import scala.concurrent.ExecutionContext.Implicits.global
/*
  val tt = ElasticSearchQueryProgram.searchNumbers(List(("", 4344545240L))).interpret[Future]
  val result = Await.result(tt, 30 seconds)
  println(result)*/

  val future = ElasticSearchQueryProgram.createNumbers(List(("", 4344545240L),("", 4344545123L))).interpret[Future]
  val futureResult = Await.result(future, 30 seconds)
  println(futureResult)
}


trait ElasticClient extends App {
  implicit val client: HttpClient = HttpClient(ElasticsearchClientUri("localhost", 9200))
}
