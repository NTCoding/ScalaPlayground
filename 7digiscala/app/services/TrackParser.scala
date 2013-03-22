package services

import org.w3c.dom.Document
import models.Track
import org.w3c.dom.Element
import scala.xml.XML
import scala.xml.Node
import scala.xml.NodeSeq

object TrackParser 
{
    def parseChartItem(body: String) : Seq[Track] =
    {
	val trackTags = XML.loadString(body) \\ "chartItem"
        val tracks = trackTags.map(tag => buildTrack(tag))
        tracks
    }

    def parseSearchResult(body: String) : Seq[Track] =
    {
      val trackTags = XML.loadString(body) \\ "searchResult"
      val tracks = trackTags.map(tag => buildTrack(tag))
      tracks
    }
	
    private def buildTrack(tag: Node) : Track =
    {
    	val title = tag \ "title" text
       	val artist = tag \ "track" \ "artist" \ "name" text
       	val image = tag \ "track" \ "release" \ "image" text
        val artistUrl = tag \ "artist" \ "url" text
       					    	
        new Track(title, artist, image, artistUrl)
    }
}
