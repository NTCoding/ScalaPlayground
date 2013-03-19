package controllers

import play.api._
import play.api.mvc._
import models._


object Application extends Controller {
  
  def index = Action {
    // create some dummy DTOs and send them to the view
    val t = new Track("amazing", "your mom", "http://www.google.com")
    Ok(views.html.index(t.title))
  }
  
}