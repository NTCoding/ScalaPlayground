package tableSpecs

import smokaz._
import scala.concurrent.duration._
import akka.testkit.TestProbe
import akka.actor.Props

class First_smoker_gets_item extends TableSpec("tableSpec1") {
  private val testTimeout = 0.1 seconds

  "If an item is put on the table" - {
    val listener = SubscribeToTableEvents(system)
    val table = system.actorOf(Props(classOf[Table]))
    table ! "Prepare"
    listener.expectMsgClass(testTimeout, classOf[TableAvailable])

    system.eventStream.publish(new PlacedCigaretteOnTable)

    "1. The item will be visible on the table" in {
      listener.expectMsgClass(testTimeout, classOf[CigaretteOnTable])
    }

    "2. The smoker who gets to the table first gets the item" in {
      val smoker1 = TestProbe()
      system.eventStream.publish(GrabCigarette(smoker1.ref))
      smoker1.expectMsgClass(testTimeout, classOf[Cigarette])

      val smoker2 = TestProbe()
      system.eventStream.publish(GrabCigarette(smoker2.ref))
      smoker2.expectNoMsg()
    }
    // TODO - test case for each different type (generic fixture or table-driven)
  }
}
