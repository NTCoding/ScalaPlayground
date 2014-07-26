package tableSpecs

import smokaz._
import scala.concurrent.duration._
import akka.testkit.TestProbe
import akka.actor.Props

class Cannot_pick_up_items_that_are_not_on_the_table extends TableSpec("tableSpec2") {
  private val testTimeout = 0.8 seconds

  "If a smoker tries to pick up an item that is no longer on the table" - {
    val smoker = TestProbe()
    val listener = SubscribeToTableEvents(system)
    val table = system.actorOf(Props(classOf[Table]))
    table ! "Prepare"
    listener.expectMsgClass(classOf[TableAvailable])

    // place and pick up 1 cigarette
    system.eventStream.publish(new PlacedCigaretteOnTable)
    listener.expectMsgClass(classOf[CigaretteOnTable])
    system.eventStream.publish(GrabCigarette(smoker.ref))
    smoker.expectMsgClass(testTimeout, classOf[Cigarette])

    // nothing on the table to pick up now
    system.eventStream.publish(GrabCigarette(smoker.ref))
    system.eventStream.publish(GrabMatch(smoker.ref))
    system.eventStream.publish(GrabPaper(smoker.ref))

    "Nothing happens" in {
      smoker.expectNoMsg(testTimeout)
      listener.expectNoMsg(testTimeout)
    }
  }
}
