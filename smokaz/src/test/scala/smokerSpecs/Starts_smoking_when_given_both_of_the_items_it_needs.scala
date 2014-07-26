package smokerSpecs

import scala.concurrent.duration._
import akka.actor.Props
import smokaz._
import dealerSpecs.ZeroSecondTimeoutGenerator

class Starts_smoking_when_given_both_of_the_items_it_needs extends SmokerSpec("smokerSpec2") {
  private val testTimeout = 0.1 seconds

  "If a smoker has 1 item" - {
    val listener = SubscribeToSmokerEvents(system)
    val smoker = system.actorOf(Props(classOf[Smoker], Set(new Paper), new ZeroSecondTimeoutGenerator))
    smoker ! "DealerIsDealing"
    listener.expectMsg(IAmCraving(smoker))

    "And then picks up an item it needs" - {
      smoker ! new Match
      listener.expectMsgClass(testTimeout, classOf[PickedUpMatch])

      "And then picks up the final item it needs" - {
        smoker ! new Cigarette
        listener.expectMsgClass(testTimeout, classOf[PickedUpCigarette])

        "The smoker starts smoking" in {
          listener.expectMsg(testTimeout, IAmSmoking(smoker))
        }
      }
    }
  }
  // TODO - test all combos
}
