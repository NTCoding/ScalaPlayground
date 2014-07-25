import akka.actor.{ActorLogging, Actor}
import akka.actor.Actor.Receive
import akka.event.EventBus

import scala.util.Random

class Dealer extends Actor with ActorLogging {
  private var canPlaceCig = true
  private var canPlaceMatch = true
  private var canPlacePaper = true

  private val placedCig = () => new PlacedCigaretteOnTable
  private val placedMatch = () => new PlacedMatchOnTable
  private val placedPaper = () => new PlacedPaperOnTable

  def receive = {
    case "StartDealing" =>
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
    case p: PickedUpCigarette =>
      placeItemOnTable()

    case p: PickedUpMatch =>
      placeItemOnTable()

    case p: PickedUpPaper =>
      placeItemOnTable()

    case n: NeedCigarette =>
      canPlaceCig = true
      placeItemOnTable()

    case n: NeedMatch =>
      canPlaceMatch = true
      placeItemOnTable()

    case n: NeedPaper =>
      canPlacePaper = true
      placeItemOnTable()
  }

  private def placeItemOnTable() {
    var availableItems = Seq.empty[String]
    if (canPlaceCig) availableItems = availableItems :+ "Cigarette"
    if (canPlaceMatch) availableItems = availableItems :+ "Match"
    if (canPlacePaper) availableItems = availableItems :+ "Paper"
    if (!availableItems.isEmpty) {
      val item = Random.shuffle(availableItems).head
      disablePlacement(item)
      log.info(s"Placed $item on table")
      context.system.eventStream.publish(mapping(item))
    }
  }

  private def disablePlacement(item: String) = item match {
    case "Cigarette" =>
      canPlaceCig = false
    case "Match" =>
      canPlaceMatch = false
    case "Paper" =>
      canPlacePaper = false
  }

  private val mapping: Map[String, AnyRef] = Map(
    "Cigarette" -> placedCig(),
    "Match" -> placedMatch(),
    "Paper" -> placedPaper()
  )
}


