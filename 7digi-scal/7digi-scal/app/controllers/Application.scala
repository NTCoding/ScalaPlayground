package controllers

import play.api._
import play.api.mvc._
import play.libs._
import models._


object Application extends Controller
{
  
    def index = Action
    {
      Ok(views.html.index(get7digitalTrackChart()))
    }


    private def get7digitalTrackChart(): List[Track] =
    {
      val chartUrl = "http://api.7digital.com/1.2/track/chart?period=week&toDate=20120301&oauth_consumer_key=YOUR_KEY_HERE&country=GB&pagesize=2"
      val response = WS.url(chartUrl).get.get().getBody
      val tracks =  parseTracks(response)
      tracks
    }

    private def parseTracks(body: String) : List[Track] =
     {
        WS.url("").
        List( new Track("wicked", "big", "blazin"))
    }

 }