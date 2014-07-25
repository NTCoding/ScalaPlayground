package dealerSpecs

import akka.actor.{Actor, Props, ActorSystem}
import akka.testkit.{TestProbe, ImplicitSender, TestKit}
import java.util
import org.scalatest.{BeforeAndAfterAll, SequentialNestedSuiteExecution, MustMatchers, FreeSpecLike}
import scala.concurrent.duration._
import smokaz._
import scala.collection.JavaConverters._

class Does_nothing_whilst_waiting_for_a_batch extends DealerSpec("dealerSpecs1")  {
  val testTimeout = 0.3 seconds // can play around with test kit time factor if necessary

  "The dealer will not do anything until it receives a batch of items" in {
    system.actorOf(Props(classOf[Dealer], new ZeroSecondTimeoutGenerator, null))

    val listener = SubscribeToEventStreamForDealerMessages(system)

    system.eventStream.publish(new PickedUpCigarette)
    system.eventStream.publish(new PickedUpMatch)
    system.eventStream.publish(new NeedPaper)

    listener.expectNoMsg(testTimeout)
  }
}

class Puts_item_on_table_when_receives_a_batch extends DealerSpec("dealerSpecs2") {
  val testTimeout = 0.3 seconds

  "When the dealer receives a batch of smoking items" - {
    val selector = new HardCodedSmokingItemSelector(new Cigarette)
    val dealer = system.actorOf(Props(classOf[Dealer], new ZeroSecondTimeoutGenerator, selector))
    val listener = SubscribeToEventStreamForDealerMessages(system)
    dealer ! "BatchOfSmokingItems"

    "It will put an item on the table that the selector chooses" in {
      listener.expectMsgClass(testTimeout, classOf[PlacedCigaretteOnTable])
    }
  }

}

class Puts_item_on_table_when_a_smoker_picks_up extends DealerSpec("dealerSpecs3") {
  val testTimeout = 0.5 seconds

  "If the dealer has put an item on the table" - {
    val selector = new QueuedSmokingItemSelector(Seq(new Cigarette, new Match))
    val dealer = system.actorOf(Props(classOf[Dealer], new ZeroSecondTimeoutGenerator, selector))
    val listener = SubscribeToEventStreamForDealerMessages(system)
    dealer ! "BatchOfSmokingItems"

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

class Only_1_item_allowed_on_the_table extends DealerSpec("dealerSpecs4") {
  val testTimeout = 0.3 seconds

  "If there is an item on the table" - {
    val selector = new HardCodedSmokingItemSelector(new Paper)
    val listener = SubscribeToEventStreamForDealerMessages(system)
    val dealer = system.actorOf(Props(classOf[Dealer], new ZeroSecondTimeoutGenerator, selector))
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

abstract class DealerSpec(name: String) extends TestKit(ActorSystem.create(name)) with FreeSpecLike with MustMatchers with BeforeAndAfterAll {
  override def afterAll() {
    system.shutdown()
  }
}



object SubscribeToEventStreamForDealerMessages {
  def apply(system: ActorSystem): TestProbe = {
    val listener = TestProbe()(system)
    system.eventStream.subscribe(listener.ref, classOf[DealerProtocol])
    listener
  }
}

class EventListener(events: java.util.ArrayList[AnyRef]) extends Actor {
  def receive = {
    case any: AnyRef =>
      events.add(any)
  }
}

class ZeroSecondTimeoutGenerator extends TimeoutGenerator {
  def generate(): FiniteDuration = 0 seconds
}

class HardCodedSmokingItemSelector(item: SmokingItem) extends SmokingItemSelector {
  def select(): SmokingItem = item
}

class QueuedSmokingItemSelector(private var items: Seq[SmokingItem]) extends SmokingItemSelector {
  def select(): SmokingItem = {
    val next = items.head
    items = items.tail
    next
  }
}
