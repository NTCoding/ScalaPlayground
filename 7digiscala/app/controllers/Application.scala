package controllers


import play.api.mvc._
import models._
import play.libs._
import org.w3c._
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.Element


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
                       .get
                       .get()
                       .asXml()

      val tracks =  parseTracks(response)
      tracks
    }

    private def parseTracks(body: Document) : Seq[Track] =
    {
        val trackTags = body.getElementsByTagName("track")
        val tracks = (0 until trackTags.getLength()).map(i => buildTrack(trackTags.item(i).asInstanceOf[Element]))
        tracks
    }

    private def buildTrack(tag: Element) : Track =
    {
    	val title = tag
    				  .getElementsByTagName("title")
    				  .item(0)
    				  .getTextContent()
    				  
    	val artist = tag
    					.getElementsByTagName("artist")
    					.item(0)
    					.asInstanceOf[Element]
    					.getElementsByTagName("name")
    					.item(0)
    					.getTextContent()
    					
       val image = tag
       				 .getElementsByTagName("image")
       				 .item(0)
       				 .getTextContent()
    	
        new Track(title, artist, image)
    }
 }