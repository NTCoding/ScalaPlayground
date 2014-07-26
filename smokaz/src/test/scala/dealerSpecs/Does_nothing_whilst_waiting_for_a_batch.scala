package dealerSpecs

import scala.concurrent.duration._
import smokaz._
import akka.actor.Props

class Does_nothing_whilst_waiting_for_a_batch extends DealerSpec("dealerSpecs1")  {
   val testTimeout = 0.3 seconds // can play around with test kit time factor if necessary

   "The dealer will not do anything until it receives a batch of items" in {
     system.actorOf(Props(classOf[Dealer], null))

     val listener = SubscribeToEventStreamForDealerMessages(system)

     system.eventStream.publish(new PickedUpCigarette)
     system.eventStream.publish(new PickedUpMatch)
     system.eventStream.publish(new NeedPaper)

     listener.expectNoMsg(testTimeout)
   }
 }
