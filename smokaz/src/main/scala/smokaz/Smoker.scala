package smokaz

import akka.actor.{ActorLogging, Actor}

import scala.util.Random
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class Smoker(private var items: Set[SmokingItem], private val timeout: TimeoutGenerator) extends Actor with ActorLogging {
  private val itemsIAlwaysHave = items

  def allSmokingItems = Seq(new Cigarette, new Match, new Paper) // TODO - don't like this, it will do for now (use enum instead)

  def receive = {
    case "DealerIsDealing" =>
      log.info(s"I am eagerly watching the table mr dealer. Please feed my cravings; I need: ${iNeed.map(_.getClass.getSimpleName) mkString ", "}")
      subscribeToItemsINeed()
      context.system.eventStream.publish(new IAmCraving(self))
      context.become(craving)
  }

  def craving: Receive = {
    case p: PaperOnTable =>
      context.system.eventStream.publish(new GrabPaper(self))

    case m: MatchOnTable =>
      context.system.eventStream.publish(new GrabMatch(self))

    case c: CigaretteOnTable =>
      context.system.eventStream.publish(new GrabCigarette(self))

    case s: SmokingItem =>
      context.system.eventStream.unsubscribe(self, availabilityNotificationFor(s))
      context.system.eventStream.publish(receiptNotification(s))
      items = items + s
      if (iHaveAllItems) startSmoking()

  }

  private def startSmoking() {
    context.become(smoking)
    context.system.eventStream.publish(IAmSmoking(self))
    log.info(s"mmm mmm mmmmm nic-o-tine baby - I am smoking")
    context.system.scheduler.scheduleOnce(timeout.generate(), self, "StartCraving")
    items = itemsIAlwaysHave
    clearSubscriptions()
  }

  def smoking: Receive = {
    case "StartCraving" =>
      log.info(s"zzzt zzzt. feed me nicotine yo. I need: ${iNeed.map(_.getClass.getSimpleName) mkString ", "}")
      subscribeToItemsINeed()
      startCraving()
      context.become(craving)

    case any =>
      log.error(s"I'm smoking. I don't want: $any")
  }

  private def iNeed() = allSmokingItems.diff(items.toSeq)

  private def iHaveAllItems = items.size == 3

  private def clearSubscriptions() {
    context.system.eventStream.unsubscribe(self)
  }

  private def subscribeToItemsINeed() {
    iNeed().foreach(item => context.system.eventStream.subscribe(self, availabilityNotificationFor(item)))
  }

  // TODO could do this when the actor starts - then dealer becomes decoupled from number of smokers
  private def startCraving() {
    iNeed().foreach(item => context.system.eventStream.publish(cravingFor(item)))
  }

  private def availabilityNotificationFor(item: SmokingItem) = item match {
    case p: Paper =>
      classOf[PaperOnTable]

    case m: Match =>
      classOf[MatchOnTable]

    case c: Cigarette =>
      classOf[CigaretteOnTable]
  }

  private def cravingFor(item: SmokingItem) = item match {
    case p: Paper =>
      new NeedPaper

    case m: Match =>
      new NeedMatch

    case c: Cigarette =>
      new NeedCigarette
  }

  private def receiptNotification(item: SmokingItem) = item match {
    case p: Paper =>
      new PickedUpPaper

    case m: Match =>
      new PickedUpMatch

    case c: Cigarette =>
      new PickedUpCigarette
  }
}


