package abtechsoft.algebra

import com.sksamuel.elastic4s.http.{HttpExecutable, RequestSuccess}
import freestyle.free.free

import scala.concurrent.ExecutionContext

@free trait ElasticSearchAlgebra {
  def execute[T, U](request: T)(implicit exec: HttpExecutable[T, U],
                                executionContext: ExecutionContext = ExecutionContext.Implicits.global): FS[RequestSuccess[U]]
}