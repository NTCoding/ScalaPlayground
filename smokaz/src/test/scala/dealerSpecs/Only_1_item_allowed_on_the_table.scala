package dealerSpecs

import scala.concurrent.duration._
import smokaz._
import akka.actor.Props

class Only_1_item_allowed_on_the_table extends DealerSpec("dealerSpecs4") {
   val testTimeout = 0.3 seconds

   "If there is an item on the table" - {
     val selector = new HardCodedSmokingItemSelector(new Paper)
     val listener = SubscribeToEventStreamForDealerMessages(system)
     val dealer = system.actorOf(Props(classOf[Dealer], selector))
     dealer ! "BatchOfSmokingItems"

     "When a smoker finishes smoking (or lies about picking up the wrong item)" - {
       system.eventStream.publish(new NeedCigarette)
       system.eventStream.publish(new NeedCigarette)
       system.eventStream.publish(new NeedMatch)
       system.eventStream.publish(new NeedMatch)
       system.eventStream.publish(new NeedPaper)
       system.eventStream.publish(new NeedPaper)
       system.eventStream.publish(new PickedUpCigarette)
       system.eventStream.publish(new PickedUpMatch)

       "The dealer will not put another item on the table - there can only be 1 item on the table" in {
         var numberOfMessageSoFar = 0
         listener.receiveWhile(testTimeout) {
           case any =>
             any.getClass must equal(classOf[PlacedPaperOnTable]) // first message when started
             numberOfMessageSoFar must equal(0)
             numberOfMessageSoFar += 1
         }
       }
     }
   }

 }
