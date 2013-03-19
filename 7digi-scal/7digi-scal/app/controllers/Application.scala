package controllers

import play.api._
import play.api.mvc._
import models._


object Application extends Controller {
  
  def index = Action {
    // create some dummy DTOs and send them to the view
    val chart = List( new Track("amazing", "your mom", "http://www.google.com"), new Track("brilliant", "your dad", "http://www.google.com"))
    Ok(views.html.index(chart))
  }
  
}