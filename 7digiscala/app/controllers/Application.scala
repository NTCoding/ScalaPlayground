package controllers



import models._
import play.libs.WS
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.Element
import services._
import play.api.libs.json.Json
import play.api.mvc.Controller
import play.api.mvc.Action



object Application extends Controller
{
  
    def index = Action  { implicit request =>
    		val chart = get7digitalTrackChart()
    		render{
    			case Accepts.Html() => Ok(views.html.index(chart))
    			case Accepts.Json() => Ok(Json.obj("tracks" -> Json.arr(
    					chart.map(t => Json.obj("title" -> t.title, "artist" -> t.artist, "artistUrl" -> t.artistUrl, "image" -> t.image))
    			)))
    		}
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