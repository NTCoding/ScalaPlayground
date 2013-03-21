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
  def search(term: String) = Action {request =>
     val response = WS.url("http://api.7digital.com/1.2/track/search")
    		  		   .setQueryParameter("q", term)
    		  		   .setQueryParameter("oauth_consumer_key", "test-api")
    		  		   .setQueryParameter("pagesize", "10")
    		  		   .get
    		  		   .get()
    		  		   .asXml()
    		  		   
      val tracks = TrackParser.parse(response)
      Ok(views.html.search(tracks))
  }
}