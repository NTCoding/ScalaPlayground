package smokaz

import akka.actor.{ActorRef, ActorLogging, Actor}

class Table extends Actor with ActorLogging {
  private var itemOnTable: Option[SmokingItem] = None

  def receive = {
    case "Prepare" =>
      log.info(s"Table now available")
      subscribeToPickups()
      context.become(available)
      context.system.eventStream.publish(new TableAvailable)
  }

  def available: Receive = {
    case c: PlacedCigaretteOnTable =>
      putOnTable(new Cigarette)
      clearSubscriptions()
      context.system.eventStream.subscribe(self, classOf[GrabCigarette])
      context.system.eventStream.publish(new CigaretteOnTable)
      context.become(waitingForCigarettePickup)

    case m: PlacedMatchOnTable =>
      putOnTable(new Match)
      clearSubscriptions()
      context.system.eventStream.subscribe(self, classOf[GrabMatch])
      context.system.eventStream.publish(new MatchOnTable)
      context.become(waitingForMatchPickup)

    case p: PlacedPaperOnTable =>
      putOnTable(new Paper)
      clearSubscriptions()
      context.system.eventStream.subscribe(self, classOf[GrabPaper])
      context.system.eventStream.publish(new PaperOnTable)
      context.become(waitingForPaperPickup)
  }

  def waitingForCigarettePickup: Receive = {
    case GrabCigarette(who) =>
      passItemAndWaitForNext(who)
  }

  def waitingForMatchPickup: Receive = {
    case GrabMatch(who) =>
      passItemAndWaitForNext(who)
  }

  def waitingForPaperPickup: Receive = {
    case GrabPaper(who) =>
      passItemAndWaitForNext(who)
  }

  private def passItemAndWaitForNext(who: ActorRef) {
    log.info(s"${who.path} got the ${itemOnTable.getOrElse("??").toString.replace("()", "")} off the table")
    subscribeToPickups()
    who ! itemOnTable.get
    itemOnTable = None
    context.become(available)
  }

  private def subscribeToPickups() {
    context.system.eventStream.subscribe(self, classOf[PlacedCigaretteOnTable])
    context.system.eventStream.subscribe(self, classOf[PlacedMatchOnTable])
    context.system.eventStream.subscribe(self, classOf[PlacedPaperOnTable])
  }

  private def clearSubscriptions() {
    context.system.eventStream.unsubscribe(self)
  }

  private def putOnTable(item: SmokingItem) {
    itemOnTable = Some(item)
    log.info(s"${item.toString.replace("()", "")} now on table")
  }

}
