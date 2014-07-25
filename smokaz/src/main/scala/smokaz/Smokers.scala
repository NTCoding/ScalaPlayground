package smokaz

import akka.actor.{ActorLogging, Actor}

import scala.util.Random
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class NeedACigSmoker extends Actor with ActorLogging {
  private val r = new Random()

  def receive = {
    case "StartSmoking" =>
      log.info(s"I neeeeed a cigarette. Feed my lungs with tar")
      context.system.eventStream.subscribe(self, classOf[PlacedCigaretteOnTable])
      context.become(waiting)
  }

  def waiting: Receive = {
    case p: PlacedCigaretteOnTable =>
      log.info("I am now smoking leave me alone")
      context.become(smoking)
      context.system.eventStream.publish(new PickedUpCigarette)
      context.system.scheduler.scheduleOnce(r.nextInt(10) seconds, self, "StopSmoking")
  }

  def smoking: Receive = {
    case p: PlacedCigaretteOnTable =>
      log.info("I am smoking you fool - do not put a cigarette on the table")

    case "StopSmoking" =>
      context.become(waiting)
      log.info("Finished smoking. Feed me more tar")
      context.system.eventStream.publish(new NeedCigarette)
  }

}

class NeedAMatchSmoker extends Actor with ActorLogging {
  private val r = new Random()

  def receive = {
    case "StartSmoking" =>
      log.info(s"I neeeeed a match. Feed my lungs with tar")
      context.system.eventStream.subscribe(self, classOf[PlacedMatchOnTable])
      context.become(waiting)
  }

  def waiting: Receive = {
    case p: PlacedMatchOnTable =>
      log.info("I am now smoking leave me alone")
      context.become(smoking)
      context.system.eventStream.publish(new PickedUpMatch)
      context.system.scheduler.scheduleOnce(r.nextInt(10) seconds, self, "StopSmoking")
  }

  def smoking: Receive = {
    case p: PlacedMatchOnTable =>
      log.info("I am smoking you fool - do not put a match on the table")


    case "StopSmoking" =>
      context.become(waiting)
      log.info("Finished smoking. Feed me more tar")
      context.system.eventStream.publish(new NeedMatch)
  }

}

class NeedPaperSmoker extends Actor with ActorLogging {
  private val r = new Random()

  def receive = {
    case "StartSmoking" =>
      log.info(s"I neeeeed some paper. Feed my lungs with tar")
      context.system.eventStream.subscribe(self, classOf[PlacedPaperOnTable])
      context.become(waiting)
  }

  def waiting: Receive = {
    case p: PlacedPaperOnTable =>
      log.info("I am now smoking leave me alone")
      context.become(smoking)
      context.system.eventStream.publish(new PickedUpPaper)
      context.system.scheduler.scheduleOnce(r.nextInt(10) seconds, self, "StopSmoking")
  }

  def smoking: Receive = {
    case p: PlacedPaperOnTable =>
      log.info("I am smoking you fool - do not put a match on the table")

    case "StopSmoking" =>
      context.become(waiting)
      log.info("Finished smoking. Feed me more tar")
      context.system.eventStream.publish(new NeedPaper)
  }

}


