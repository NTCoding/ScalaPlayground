package test

import com.ning.http.client._
import org.scalatest.{FreeSpec, MustMatchers}
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import com.codahale.jerkson.Json

class Geo_locator_specs extends FreeSpec with MustMatchers {

  "Search examples" - {
    val locator = GeoLocator(new AsyncHttpClient())

    "When searching for London" - {
      val result = Await.result(locator.locate("London"), 30 seconds)

      "London's latitude and longitude are returned" in {
        val londonLat = "51.5073509"
        val londonLon = "-0.1277583"
        result.lat must equal(londonLat)
        result.lon must equal(londonLon)
      }
    }
  }
}

object GeoLocator {
  def apply(client: AsyncHttpClient) = new GeoLocator(client)

  implicit def toAsyncHandler[A](func: Response => A): AsyncHandler[Response] = {
    AsyncCompletionHandlerWithFuture(func)
  }

}

class GeoLocator(client: AsyncHttpClient) {
  import test.GeoLocator.toAsyncHandler

  def locate(location: String): Future[GeoLocationSearchResponse] = {
    val url = urlFor(location)
    val p = Promise[GeoLocationSearchResponse]()
    client.prepareGet(url).execute{ (response: Response) =>
      val gres = Json.parse[GoogleGeoLocationApiResponse](response.getResponseBody)
      val res = GeoLocationSearchResponse(gres.results.head.geometry.location.lat.toString, gres.results.head.geometry.location.lng.toString)
      p.completeWith(Future.successful(res))
    }
    p.future
  }

  private def urlFor(location: String) = s"https://maps.googleapis.com/maps/api/geocode/json?address=$location"
}

case class GeoLocationSearchResponse(lat: String, lon: String)

case class AsyncCompletionHandlerWithFuture[A](func: Response => A) extends AsyncCompletionHandler[Response] {
  override def onCompleted(response: Response): Response = {
    func(response)
    response
  }
}

case class GoogleGeoLocationApiResponse(results: Seq[Result])
case class Result(geometry: Geometry)
case class Geometry(location: Location)
case class Location(lat: BigDecimal, lng: BigDecimal)
