package tableSpecs

import smokaz._
import akka.testkit.TestProbe
import akka.actor.Props
import scala.concurrent.duration._

class Multiple_items_not_allowed_on_table extends TableSpec("tableSpec3") {
 private val testTimeout = 0.3 seconds

 "If an item is currently on the table" - {
   val listener = SubscribeToTableEvents(system)
   val smoker = TestProbe()
   val table = system.actorOf(Props(classOf[Table]))
   table ! "Prepare"
   listener.expectMsgClass(testTimeout, classOf[TableAvailable])
   system.eventStream.publish(new PlacedPaperOnTable)

   "Attempts to place new items on the table" - {
     system.eventStream.publish(new PlacedMatchOnTable)
     system.eventStream.publish(new PlacedCigaretteOnTable)
     system.eventStream.publish(new PlacedPaperOnTable)

     "Will not be successful" in {
       system.eventStream.publish(GrabPaper(smoker.ref))
       system.eventStream.publish(GrabMatch(smoker.ref))
       system.eventStream.publish(GrabCigarette(smoker.ref))
       system.eventStream.publish(GrabCigarette(smoker.ref))
       smoker.expectMsgClass(testTimeout, classOf[Paper])
       smoker.expectNoMsg(testTimeout)
     }
   }
 }

}
