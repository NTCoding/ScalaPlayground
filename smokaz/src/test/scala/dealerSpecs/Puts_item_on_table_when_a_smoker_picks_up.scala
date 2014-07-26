package dealerSpecs

import scala.concurrent.duration._
import smokaz._
import akka.actor.Props

class Puts_item_on_table_when_a_smoker_picks_up extends DealerSpec("dealerSpecs3") {
   val testTimeout = 0.5 seconds

   "If the dealer has put an item on the table" - {
     val selector = new QueuedSmokingItemSelector(Seq(new Cigarette, new Match))
     val listener = SubscribeToEventStreamForDealerMessages(system)
     val dealer = system.actorOf(Props(classOf[Dealer], selector))

     dealer ! "BatchOfSmokingItems"
     Thread.sleep(100)

     "When a smoker picks up the item from the table" - {
       system.eventStream.publish(new PickedUpCigarette)

       "The dealer will put an item on the table that the selector chooses" in {
         val msgs = listener.receiveN(2, testTimeout)
         msgs.length must equal(2)
         msgs.head.getClass must equal(classOf[PlacedCigaretteOnTable]) // first message when table is initially empty
         msgs.tail.head.getClass must equal(classOf[PlacedMatchOnTable])
       }
     }
   }
 }
