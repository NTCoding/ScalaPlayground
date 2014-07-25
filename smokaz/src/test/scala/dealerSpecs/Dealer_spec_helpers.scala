package dealerSpecs

import akka.actor.{Actor, ActorSystem}
import akka.testkit.{TestProbe, TestKit}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, FreeSpecLike}
import scala.concurrent.duration._
import smokaz._

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
