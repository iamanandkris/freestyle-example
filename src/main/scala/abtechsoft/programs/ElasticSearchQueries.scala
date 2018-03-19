package abtechsoft.programs

import abtechsoft.algebra.ElasticSearchAlgebra
import com.sksamuel.elastic4s.http.{HttpExecutable, RequestSuccess}
import com.sksamuel.elastic4s.http.search.{MultiSearchResponse, SearchResponse}
import com.sksamuel.elastic4s.searches.{MultiSearchDefinition, SearchDefinition}
import freestyle.free.FreeS
import freestyle.free._
import freestyle.free.implicits._
import scala.concurrent.ExecutionContext

trait ElasticSearch[T, R] {

  def request(data: T): SearchDefinition

  def responseMapper(response: RequestSuccess[SearchResponse]): R
}

trait ElasticMultiSearch[T, R] {

  def request(data: T): MultiSearchDefinition

  def responseMapper(response: RequestSuccess[MultiSearchResponse]): R
}

class ElasticSearchQueries[F[_]] {
  def search[T, R](data: T)(implicit requestMapper: ElasticSearch[T, R],
                            app: ElasticSearchAlgebra[F],
                            executable: HttpExecutable[SearchDefinition, SearchResponse],
                            ec: ExecutionContext): FreeS[F, R] = {
    for {
      request <- FreeS.pure(requestMapper.request(data))
      response <- app.execute(request)
      result <- FreeS.pure(requestMapper.responseMapper(response))
    } yield result
  }

  def multiSearch[T, R](data: T)(implicit requestMapper: ElasticMultiSearch[T, R],
                            app: ElasticSearchAlgebra[F],
                            executable: HttpExecutable[MultiSearchDefinition, MultiSearchResponse],
                            ec: ExecutionContext): FreeS[F, R] = {
    for {
      request <- FreeS.pure(requestMapper.request(data))
      response <- app.execute(request)
      result <- FreeS.pure(requestMapper.responseMapper(response))
    } yield result
  }

}
