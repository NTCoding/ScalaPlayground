package smokerSpecs

import scala.concurrent.duration._
import akka.actor.Props
import smokaz._
import dealerSpecs.ZeroSecondTimeoutGenerator

class Tries_to_grab_item_it_needs_from_table extends SmokerSpec("smokerSpec1") {
  private val testTimeout = 0.3 seconds

  "If a smoker only has 1 item it needs for smoking" - {
    val smoker = system.actorOf(Props(classOf[Smoker], Set(new Cigarette), new ZeroSecondTimeoutGenerator))
    val listener = SubscribeToSmokerEvents(system)
    smoker ! "DealerIsDealing"
    listener.expectMsg(IAmCraving(smoker))

    "When an item it needs is on the table" - {
      system.eventStream.publish(new PaperOnTable)

      "It trys to pick it up off the table" in {
        listener.expectMsg(testTimeout, new GrabPaper(smoker))
      }
    }

    // TODO - test cases for each type (generic test fixture or table-driven specs)
  }

}
