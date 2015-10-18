package controllers

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.{ ask, pipe }
import akka.util.Timeout
import play.libs.Akka
import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  implicit val askTimeout: Timeout = 30 seconds

  def findHolidayPackage(location: String) = Action.async { request =>
  	// ? == ask   <- returns a Future[Any]
  	(ActorSystem.searcher ? FindHolidayPackage(location)) map {
  		case p: Package =>
  			Ok(views.html.showPackage(p))
  	} recoverWith {
  		case e: Exception =>
  			Future.successful(Ok(views.html.errorFetchingPackage()))
  	}
  }

}

object ActorSystem {
	private val system = Akka.system
	private val holidayProviders: Seq[String] = Seq("http://xxx.fail")
	private val flightProviders: Seq[String] = Seq("http://xxx.fail2")

	lazy val searcher: ActorRef = {
		system.actorOf(Props(classOf[PackageSearcher], holidayProviders, flightProviders))
	}
}

class PackageSearcher(holidayProviders: Seq[String], flightProviders: Seq[String]) extends Actor {
	private val workerProps = Props(classOf[SearchWorker], holidayProviders, flightProviders)

	def receive = {
		case f: FindHolidayPackage =>
			// create per-request worker so searcher performance is not impeded
			val worker = context.actorOf(workerProps) // becomes a child of this actor
			worker forward f // forward the request so errors are returned all the way up to the controller
	}

}

class SearchWorker(holidayProviders: Seq[String], flightProviders: Seq[String]) extends Actor {
	// search workers are per-reqest - it is ok to keep private state
	// null used for demonstration and not recommended
	private var recipient: ActorRef = null
	private var flight: Flight = null
	private var holiday: Holiday = null

  // start off in the awaiting state
	def receive = awaitingSearchRequest

	def awaitingSearchRequest: Receive = {
		case f: FindHolidayPackage =>
			recipient = sender() // will fulfill the original ask 
			context.become(awaitingHolidayAndFlight) // now in a different state
			holidayProviders.foreach(hp => searchForHoliday(hp, f.location))
			flightProviders.foreach(fp => searchForFlight(fp, f.location))
	}

  // will not accept FindHolidayPackage messages in this state
	private def awaitingHolidayAndFlight: Receive = {
		case h: Holiday =>
			context.become(awaitingFlight) 
			holiday = h
		case f: Flight =>
			context.become(awaitingHoliday) 
			flight = f
	}

	// ignore further holidays - first one is good enough
	private def awaitingFlight: Receive = {
		case f: Flight =>
			recipient ! Package(holiday, f)
			context.stop(self)
	}

  // ignore further flights - first one is good enough
	private def awaitingHoliday: Receive = {
		case h: Holiday =>
			recipient ! Package(h, flight)
			context.stop(self)
	}

	private def searchForHoliday(provider: String, location: String) {
		// perform asycn web request
		Future { Holiday("a holiday") } pipeTo self // send future as message to self
		// Important to pipe the result of any futures otherwise they will compete
		// with the actors for threads (unless you use a different execution context)
	}

	private def searchForFlight(provider: String, location: String) {
		Future { Flight("a flight") } pipeTo self
		// The error below will make its way back up to the controller because of the "forwardTo"
		// without the forwardTo, the error would get lost and the request would time out without any diagnostics
		//recipient ! Future { throw new Exception("Error finding flight") }
	} 

}

case class FindHolidayPackage(location: String)
case class Holiday(description: String)
case class Flight(description: String)
case class Package(holiday: Holiday, flight: Flight)

