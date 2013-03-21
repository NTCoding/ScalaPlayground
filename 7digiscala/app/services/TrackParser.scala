package services

import org.w3c.dom.Document
import models.Track
import org.w3c.dom.Element

object TrackParser 
{
	def parse(body: Document) : Seq[Track] =
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
       				 
       val artistUrl = tag
       					.getElementsByTagName("artist")
       					.item(0)
       					.asInstanceOf[Element]
       					.getElementsByTagName("url")
       					.item(0)
       					.getTextContent()
    	
        new Track(title, artist, image, artistUrl)
    }
}