package smokerSpecs

import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestProbe, TestKit}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, FreeSpecLike}
import smokaz._
import scala.concurrent.duration._
import dealerSpecs.ZeroSecondTimeoutGenerator

object SubscribeToSmokerEvents {
  def apply(system: ActorSystem): TestProbe = {
    val probe = TestProbe()(system)
    system.eventStream.subscribe(probe.ref, classOf[SmokerProtocol])
    probe
  }
}

abstract class SmokerSpec(name: String) extends TestKit(ActorSystem.create(name)) with FreeSpecLike with MustMatchers with BeforeAndAfterAll {

  override def afterAll() {
    system.shutdown()
  }
}
