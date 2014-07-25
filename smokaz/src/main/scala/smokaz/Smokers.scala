package smokaz

import akka.actor.{ActorLogging, Actor}

import scala.util.Random
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class Smoker(private var items: Set[SmokingItem]) extends Actor with ActorLogging {

  def allSmokingItems = Seq(new Cigarette, new Match, new Paper) // TODO - don't like this, it will do for now (use enum instead)

  def receive = {
    case "DealerIsDealing" =>
      log.info(s"I am eagerly watching the table mr dealer. Please feed my cravings; I need: ${iNeed.map(_.getClass.getSimpleName) mkString ", "}")
      iNeed().foreach {
        t =>
          log.info(s"Subscribing to ${availabilityNotificationFor(t)}")
          context.system.eventStream.subscribe(self, availabilityNotificationFor(t))
      }
      context.become(craving)
  }

  def craving: Receive = {
    case p: PlacedPaperOnTable =>
      log.info(s"Trying to grab the paper")
      context.system.eventStream.publish(new GrabPaper(self))

    case m: PlacedMatchOnTable =>
      log.info(s"Trying to grab the match")
      context.system.eventStream.publish(new GrabMatch(self))

    case c: PlacedCigaretteOnTable =>
      log.info(s"Trying to grab the cigarette")
      context.system.eventStream.publish(new GrabCigarette(self))
  }

  private def iNeed() = allSmokingItems.diff(items.toSeq)

  private def availabilityNotificationFor(item: SmokingItem) = item match {
    case p: Paper =>
      classOf[PlacedPaperOnTable]

    case m: Match =>
      classOf[PlacedMatchOnTable]

    case c: Cigarette =>
      classOf[PlacedCigaretteOnTable]
  }
}


