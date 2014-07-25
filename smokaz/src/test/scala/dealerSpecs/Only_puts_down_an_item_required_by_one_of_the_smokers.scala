package dealerSpecs

import scala.concurrent.duration._
import smokaz._
import akka.actor.Props

class Only_puts_down_an_item_required_by_one_of_the_smokers extends DealerSpec("dealerSpecs5") {
   private val testTimeout = 0.3 seconds

   "If some of the items are not required by any of the smokers" - {
     val invalidSequenceOfItemPlacements = Seq(
       new Cigarette, new Cigarette, new Match, new Match, new Match, new Cigarette, new Match, new Paper
     )
     val selector = new QueuedSmokingItemSelector(invalidSequenceOfItemPlacements)
     val listener = SubscribeToEventStreamForDealerMessages(system)
     val dealer = system.actorOf(Props(classOf[Dealer], new ZeroSecondTimeoutGenerator, selector))
     dealer ! "BatchOfSmokingItems"

     listener.receiveOne(testTimeout).getClass must equal(classOf[PlacedCigaretteOnTable])
     system.eventStream.publish(new PickedUpCigarette)
     listener.receiveOne(testTimeout).getClass must equal(classOf[PlacedCigaretteOnTable])
     system.eventStream.publish(new PickedUpCigarette)
     listener.receiveOne(testTimeout).getClass must equal(classOf[PlacedMatchOnTable])
     system.eventStream.publish(new PickedUpMatch)
     listener.receiveOne(testTimeout).getClass must equal(classOf[PlacedMatchOnTable])
     system.eventStream.publish(new PickedUpMatch)

     "When the dealer puts an item on the table" - {
       val msg = listener.receiveOne(testTimeout)

       "It will not put down an item that is not required by any smoker" in {
         // Notice in the sequence of items there were other matches and cigarettes ignored
         msg.getClass must equal(classOf[PlacedPaperOnTable])
       }
     }
   }

 }
