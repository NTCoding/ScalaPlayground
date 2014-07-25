package smokerSpecs

import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestProbe, TestKit}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, FreeSpecLike}
import smokaz._
import scala.concurrent.duration._

class Picks_up_item_it_needs_when_only_has_one_item extends SmokerSpec("smokerSpec1") {
  private val testTimeout = 0.3 seconds

  "If a smoker only has 1 item it needs for smoking" - {
    val smoker = system.actorOf(Props(classOf[Smoker], Set(new Cigarette)))
    val listener = SubscribeToSmokerEvents(system)
    smoker ! "DealerIsDealing"

    "When an item it needs is placed on the table" - {
      Thread.sleep(100)
      system.eventStream.publish(new PlacedPaperOnTable)

      "It will try and pick it up off the table" in {
        listener.expectMsg(testTimeout, new GrabPaper(smoker))
      }
    }

    // TODO - test cases for each type (generic test fixture or table-driven specs)
  }

}

object SubscribeToSmokerEvents {
  def apply(system: ActorSystem): TestProbe = {
    val probe = TestProbe()(system)
    system.eventStream.subscribe(probe.ref, classOf[SmokerProtocol])
    probe
  }
}

 /*
  "If a smoker only has 2 items it needs for smoking" - {

    "When an item it already has is placed on the table" - {

      "It will not try and pick it up (technically it should even be notified about the event - see implementation)" in {
        fail
      }
    }

    "When the remaining item is placed on the table" - {

      "It will try and pick it up off the table" in {
        fail
      }
    }
  }

  "If a smoker has all 3 items it needs" - {

    "When any item is placed on the table" - {

      "It will not try and pick it up" in {
        fail
      }
    }
  }

  "When a smoker picks up an item from the table" - {

    "It will celebrate picking up the item" in {
      fail
    }
  }

  "When a smoker is beaten to an item on the table by another smoker" - {

    "It will have an addict fit" in {
      fail
    }
  }

  "When a smoker finishes smoking" - {

    "It will start craving for the items it needs" in {
      fail
    }
  }
}
*/

abstract class SmokerSpec(name: String) extends TestKit(ActorSystem.create(name)) with FreeSpecLike with MustMatchers with BeforeAndAfterAll {

  override def afterAll() {
    system.shutdown()
  }
}
