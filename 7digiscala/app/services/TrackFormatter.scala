package services

import models.Track
import play.api.libs.json.Json

object TrackFormatter 
{
	def toJson(chart: Seq[Track]): play.api.libs.json.JsObject = 
	{
			return Json.obj("tracks" ->
						Json.arr(chart.map(t => Json.obj("title" -> t.title, "artist" -> t.artist, "artistUrl" -> t.artistUrl, "image" -> t.image))))
    }
	
}