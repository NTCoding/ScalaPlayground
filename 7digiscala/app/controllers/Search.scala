package controllers

import play.api._
import play.libs._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import services._


object Search extends Controller 
{
		  def search(term: String) = Action {implicit request =>
		  		val response = WS.url("http://api.7digital.com/1.2/track/search")
		  										.setQueryParameter("q", term)
		  										.setQueryParameter("oauth_consumer_key", "test-api")
		  										.setQueryParameter("pagesize", "10")
		  										.setQueryParameter("imagesize", "50")
		  										.get
		  										.get()
		  										.getBody()
		    		  		   
		      val tracks = TrackParser.parseSearchResult(response)
		      render {
		  			case Accepts.Html() =>  Ok(views.html.search(tracks, term))
		  			case Accepts.Json() =>  Ok(TrackFormatter.toJson(tracks))
		  		}
		  }
}