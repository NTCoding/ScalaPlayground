package dealerSpecs

import scala.concurrent.duration._
import smokaz._
import akka.actor.Props

class Puts_item_on_table_when_receives_a_batch extends DealerSpec("dealerSpecs2") {
   val testTimeout = 0.3 seconds

   "When the dealer receives a batch of smoking items" - {
     val selector = new HardCodedSmokingItemSelector(new Cigarette)
     val dealer = system.actorOf(Props(classOf[Dealer], selector))
     val listener = SubscribeToEventStreamForDealerMessages(system)
     dealer ! "BatchOfSmokingItems"

     "It will put an item on the table that the selector chooses" in {
       listener.expectMsgClass(testTimeout, classOf[PlacedCigaretteOnTable])
     }
   }

 }
