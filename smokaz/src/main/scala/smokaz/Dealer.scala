package smokaz

import akka.actor.{ActorLogging, Actor}
import akka.actor.Actor.Receive
import akka.event.EventBus

import scala.util.Random

class Dealer(private val selector: SmokingItemSelector) extends Actor with ActorLogging {
  private var numberOfSmokersNeedingCigs = 2
  private var numberOfSmokersNeedingMatches = 2
  private var numberOfSmokersNeedingPaper = 2

  private def canPlaceCig = numberOfSmokersNeedingCigs > 0
  private def canPlaceMatch = numberOfSmokersNeedingMatches > 0
  private def canPlacePaper = numberOfSmokersNeedingPaper > 0

  // TODO - could move this responsibility to table - and table indicates when item has been picked up
  private var itemOnTable: Option[AnyRef] = None

  def receive = {
    case "BatchOfSmokingItems" =>
      log.debug("About to start dealing to the smokaz")
      context.system.eventStream.subscribe(self, classOf[PickedUpCigarette])
      context.system.eventStream.subscribe(self, classOf[PickedUpMatch])
      context.system.eventStream.subscribe(self, classOf[PickedUpPaper])
      context.system.eventStream.subscribe(self, classOf[NeedCigarette])
      context.system.eventStream.subscribe(self, classOf[NeedMatch])
      context.system.eventStream.subscribe(self, classOf[NeedPaper])
      context.become(dealing)
      placeItemOnTable()

    case anythingElse =>
      log.debug("I am not dealing right now you f**ing addicts")
  }

  def dealing: Receive = {
    case p: PickedUpCigarette if itemOnTable.exists(_.getClass == classOf[Cigarette]) =>
      itemOnTable = None
      numberOfSmokersNeedingCigs = numberOfSmokersNeedingCigs - 1
      placeItemOnTable()

    case p: PickedUpMatch if itemOnTable.exists(_.getClass == classOf[Match]) =>
      itemOnTable = None
      numberOfSmokersNeedingMatches = numberOfSmokersNeedingMatches - 1
      placeItemOnTable()

    case p: PickedUpPaper if itemOnTable.exists(_.getClass == classOf[Paper]) =>
      itemOnTable = None
      numberOfSmokersNeedingPaper = numberOfSmokersNeedingPaper - 1
      placeItemOnTable()

    case n: NeedCigarette =>
      numberOfSmokersNeedingCigs = numberOfSmokersNeedingCigs + 1
      placeItemOnTable()

    case n: NeedMatch =>
      numberOfSmokersNeedingMatches = numberOfSmokersNeedingMatches + 1
      placeItemOnTable()

    case n: NeedPaper =>
      numberOfSmokersNeedingPaper = numberOfSmokersNeedingPaper + 1
      placeItemOnTable()

    case any =>
      log.error(s"Do not do this: $any")
  }

  private def placeItemOnTable() {
    if (itemOnTable.isDefined || everyoneIsSmoking) return
    val item = selector.select()
    if (canPlace(item)) {
      log.info(s"Placed ${item.getClass.getSimpleName} on table")
      context.system.eventStream.publish(eventFor(item))
      itemOnTable = Some(item)
    }
    else {
      placeItemOnTable()
    }
  }

  private def everyoneIsSmoking() = (!canPlaceCig) && (!canPlaceMatch) && !(canPlacePaper)

  private def canPlace(item: SmokingItem) = item match {
    case c: Cigarette =>
      canPlaceCig
    case m: Match =>
      canPlaceMatch
    case p: Paper =>
      canPlacePaper
  }

  private def eventFor(item: SmokingItem) = item match {
    case c: Cigarette =>
      new PlacedCigaretteOnTable
    case m: Match =>
      new PlacedMatchOnTable
    case p: Paper =>
      new PlacedPaperOnTable
  }
}


