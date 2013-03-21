package controllers


import play.api.mvc._
import models._
import play.libs._
import org.w3c._
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.Element
import services._


object Application extends Controller
{
  
    def index = Action
    {
      Ok(views.html.index(get7digitalTrackChart()))
    }


    private def get7digitalTrackChart(): Seq[Track] =
    {
      val response = WS.url("http://api.7digital.com/1.2/track/chart")
                       .setQueryParameter("period", "week")
                       .setQueryParameter("oauth_consumer_key", "test-api")
                       .setQueryParameter("toDate", "20120301")
                       .setQueryParameter("imageSize", "100")
                       .get
                       .get()
                       .getBody()

      val tracks =  TrackParser.parseChartItem(response)
      tracks
    }

 }