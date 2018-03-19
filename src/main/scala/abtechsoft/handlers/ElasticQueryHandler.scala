package abtechsoft.handlers

import abtechsoft.algebra.ElasticSearchAlgebra
import abtechsoft.programs.ElasticSearchFailure
import cats.data.EitherT
import com.sksamuel.elastic4s.http.{HttpClient, HttpExecutable, RequestFailure, RequestSuccess}

import scala.concurrent.{ExecutionContext, Future}
import freestyle.free._
import freestyle.free.implicits._
import cats.implicits._

object ElasticQueryHandler {


  implicit def elasticHandler(implicit elastic: HttpClient): ElasticSearchAlgebra.Handler[Future] = new ElasticSearchAlgebra.Handler[Future] {
    override protected[this] def execute[T, U](request: T)(implicit exec: HttpExecutable[T, U],
                                                           executionContext: ExecutionContext): Future[RequestSuccess[U]] =
      elastic.execute(request).flatMap {
        case Left(error) => Future.failed(ElasticSearchFailure(error.error.reason))
        case Right(result) => Future.successful(result)
      }

  }

}
