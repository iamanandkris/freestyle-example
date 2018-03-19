package abtechsoft.programs.queries

import abtechsoft.programs.PureFunctions.NumberRange
import abtechsoft.programs.{ElasticMultiSearch, ElasticSearch, FindNumberResponse}
import com.sksamuel.elastic4s.IndexAndTypes
import com.sksamuel.elastic4s.http.ElasticDsl.{multi, rangeQuery, searchWithType}
import com.sksamuel.elastic4s.http.RequestSuccess
import com.sksamuel.elastic4s.http.search.{MultiSearchResponse, SearchResponse}
import com.sksamuel.elastic4s.searches.{MultiSearchDefinition, SearchDefinition}
import com.sksamuel.elastic4s.circe._
import io.circe.generic.auto._

trait ElasticSearchQueries {

  implicit object SearchNumbers extends ElasticMultiSearch[List[(String, Long)], List[FindNumberResponse]] {
    override def request(data: List[(String, Long)]): MultiSearchDefinition = {
      val queries = data.map {
        case (_, number) =>
          searchWithType(IndexAndTypes("readmodels", "service_numbers"))
            .termQuery("number", number)
            .sourceInclude("id", "number")
      }
      multi(queries)
    }

    override def responseMapper(response: RequestSuccess[MultiSearchResponse]): List[FindNumberResponse] = {
      val result = response.result
      result.to[FindNumberResponse].toList
    }
  }

  implicit object SearchNumbersByRange extends ElasticSearch[NumberRange, List[FindNumberResponse]] {
    override def request(data: NumberRange): SearchDefinition = {
      searchWithType(IndexAndTypes("readmodels", "service_numbers"))
        .query(rangeQuery("number")
          .gte(data.from)
          .lte(data.to))
        .sourceInclude("id", "number")
    }

    override def responseMapper(response: RequestSuccess[SearchResponse]): List[FindNumberResponse] = {
      val result = response.result
      result.to[FindNumberResponse].toList
    }
  }

}

object ElasticSearchQueries extends ElasticSearchQueries
