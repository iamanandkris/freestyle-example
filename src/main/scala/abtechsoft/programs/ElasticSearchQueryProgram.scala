package abtechsoft.programs

import abtechsoft.algebra.{AppModule, ElasticSearchAlgebra}
import abtechsoft.programs.PureFunctions.NumberRange
import com.sksamuel.elastic4s.IndexAndTypes
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.RequestSuccess
import com.sksamuel.elastic4s.http.search.{MultiSearchResponse, SearchResponse}
import com.sksamuel.elastic4s.searches.{MultiSearchDefinition, SearchDefinition}
import freestyle.free.FreeS
import com.sksamuel.elastic4s.circe._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext
import freestyle.free._
import freestyle.free.implicits._
import cats.implicits._

case class FindNumberResponse(id: String = "",
                              number: Long = 0L)

case class Command(n: Long)

case class ElasticSearchFailure(message: String) extends Exception

object PureFunctions {

  case class NumberRange(from: Long, to: Long)

  def buildMultiSearchNumbersQuery(numbers: List[(String, Long)]): MultiSearchDefinition = {
    val queries = numbers.map {
      case (_, number) =>
        searchWithType(IndexAndTypes("readmodels", "service_numbers"))
          .termQuery("number", number)
          .sourceInclude("id", "number")
    }
    multi(queries)
  }

  def mapMultiSearchNumbersQueryResponse(response: RequestSuccess[MultiSearchResponse]): List[FindNumberResponse] = {
    val result = response.result
    result.to[FindNumberResponse].toList
  }


  def buildNumberRangeSearchQuery(numberRange: NumberRange): SearchDefinition = {
    searchWithType(IndexAndTypes("readmodels", "service_numbers"))
      .query(rangeQuery("number")
        .gte(numberRange.from)
        .lte(numberRange.to))
      .sourceInclude("id", "number")
  }

  def mapNumberRangeSearchQueryResponse(response: RequestSuccess[SearchResponse]): List[FindNumberResponse] = {
    val result = response.result
    result.to[FindNumberResponse].toList
  }
}

object ElasticSearchQueryProgram {

  def buildMultiSearchNumbersQuery(numbers: List[(String, Long)]): MultiSearchDefinition = {
    val queries = numbers.map {
      case (_, number) =>
        searchWithType(IndexAndTypes("readmodels", "service_numbers"))
          .termQuery("number", number)
          .sourceInclude("id", "number")
    }
    multi(queries)
  }

  def mapMultiSearchNumbersQueryResponse(response: RequestSuccess[MultiSearchResponse]): List[FindNumberResponse] = {
    val result = response.result
    result.to[FindNumberResponse].toList
  }


  def buildNumberRangeSearchQuery(from: Long, to: Long): SearchDefinition = {
    searchWithType(IndexAndTypes("readmodels", "service_numbers"))
      .query(rangeQuery("number")
        .gte(from)
        .lte(to))
      .sourceInclude("id", "number")
  }

  def mapNumberRangeSearchQueryResponse(response: RequestSuccess[SearchResponse]): List[FindNumberResponse] = {
    val result = response.result
    result.to[FindNumberResponse].toList
  }

  def searchNumbers[F[_]](numbers: List[(String, Long)])
                         (implicit app: ElasticSearchAlgebra[F],
                          ec: ExecutionContext): FreeS[F, List[FindNumberResponse]] = {
    for {
      request <- FreeS.pure(buildMultiSearchNumbersQuery(numbers))
      response <- app.execute(request)
      result <- FreeS.pure(mapMultiSearchNumbersQueryResponse(response))
    } yield result

  }

  def searchNumbersByRange[F[_]](from: Long, to: Long)
                                (implicit app: ElasticSearchAlgebra[F],
                                 ec: ExecutionContext): FreeS[F, List[FindNumberResponse]] = {
    for {
      request <- FreeS.pure(buildNumberRangeSearchQuery(from, to))
      response <- app.execute(request)
      result <- FreeS.pure(mapNumberRangeSearchQueryResponse(response))
    } yield result

  }

  def createNumbers(numbers: List[(String, Long)])(implicit app: AppModule[AppModule.Op],
                                                   ec: ExecutionContext): FreeS[AppModule.Op, Command] = {
    val range = NumberRange(from = 232, to = 434)
    for {
      _ <- app.commandAlgebra.validation(numbers)
      response <- searchNumbers[AppModule.Op](numbers)
      result <- app.commandAlgebra.create(response)
    } yield result

  }

  import abtechsoft.programs.queries.ElasticSearchQueries._

  //This is a program
  def createNumber(numbers: List[(String, Long)])(implicit app: AppModule[AppModule.Op],
                                                  ec: ExecutionContext): FreeS[AppModule.Op, Command] = {
    val range = NumberRange(from = 232, to = 434)
    val queryProgram = new ElasticSearchQueries[AppModule.Op]
    for {
      _ <- app.commandAlgebra.validation(numbers)
      response <-searchNumbers[AppModule.Op](numbers)
      result <- app.commandAlgebra.create(response)
    } yield result

  }

}
