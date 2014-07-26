package smokerSpecs

import scala.concurrent.duration._
import akka.actor.Props
import smokaz._
import dealerSpecs.ZeroSecondTimeoutGenerator

class Does_not_grab_items_it_has extends SmokerSpec("smokerSpec4") {
  private val testTimeout = 0.1 seconds

  "If a smoker already has items" - {
    val listener = SubscribeToSmokerEvents(system)
    val smoker = system.actorOf(Props(classOf[Smoker], Set(new Cigarette), new ZeroSecondTimeoutGenerator))
    smoker ! "DealerIsDealing"
    listener.expectMsg(testTimeout, IAmCraving(smoker))
    smoker ! new Match
    listener.expectMsgClass(testTimeout, classOf[PickedUpMatch])

    "When an item it already has is placed on the table" - {
      system.eventStream.publish(new CigaretteOnTable)
      system.eventStream.publish(new MatchOnTable)
      system.eventStream.publish(new CigaretteOnTable)
      system.eventStream.publish(new MatchOnTable)

      "It doesn't try to grab the item" in {
        listener.expectNoMsg(testTimeout)
      }
    }
  }
  // TODO - test more combinations
}
