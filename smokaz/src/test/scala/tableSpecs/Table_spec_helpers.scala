package tableSpecs

import akka.actor._
import akka.testkit.{TestProbe, ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, FreeSpecLike}
import smokaz._

abstract class TableSpec(name: String) extends TestKit(ActorSystem.create(name)) with FreeSpecLike with MustMatchers with BeforeAndAfterAll with ImplicitSender {
  override def afterAll() {
    system.shutdown()
  }
}

object SubscribeToTableEvents {
  def apply(system: ActorSystem): TestProbe = {
    val t = TestProbe()(system)
    system.eventStream.subscribe(t.ref, classOf[TableProtocol])
    t
  }
}
