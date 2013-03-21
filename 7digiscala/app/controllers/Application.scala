package controllers


import play.api.mvc._
import models._
import play.libs._
import org.w3c._


object Application extends Controller
{
  
    def index = Action
    {
      Ok(views.html.index(get7digitalTrackChart()))
    }


    private def get7digitalTrackChart(): List[Track] =
    {
      val response = WS.url("http://api.7digital.com/1.2/track/chart")
                       .setQueryParameter("period", "week")
                       .setQueryParameter("oauth_consumer_key", "test-api")
                       .setQueryParameter("toDate", "20120301")
                       .get
                       .get()
                       .asXml()

      val tracks =  parseTracks(response)
      tracks
    }

    private def parseTracks(body: Document) : List[Track] =
    {
        val trackTags = body.getElementsByTagName("track");
        val tracks = (0 until trackTags.length).foreach({ i => buildTrack(trackTags[i]) })
        List( new Track("wicked", "big", "blazin"))
    }

    private def buildTrack(tag: Node) : Track =
    {
        new Track("title", "artist", "image")
    }
 }