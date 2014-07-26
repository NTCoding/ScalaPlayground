package smokerSpecs

import scala.concurrent.duration._
import akka.actor.Props
import smokaz._
import dealerSpecs.ZeroSecondTimeoutGenerator

class Starts_craving_after_finishes_smoking extends SmokerSpec("smokerSpec3") {
  private val testTimeout = 0.1 seconds

  "When a smoker finishes smoking" - {
    val listener = SubscribeToSmokerEvents(system)
    val smoker = system.actorOf(Props(classOf[Smoker], Set(new Match), new ZeroSecondTimeoutGenerator))
    smoker ! "DealerIsDealing"

    listener.expectMsg(testTimeout, IAmCraving(smoker))
    smoker ! new Paper
    listener.expectMsgClass(testTimeout, classOf[PickedUpPaper])

    smoker ! new Cigarette
    listener.expectMsgClass(testTimeout, classOf[PickedUpCigarette])

    listener.expectMsg(testTimeout, IAmSmoking(smoker))

    "It starts craving for the items it needs" in {
      val ms = listener.receiveN(2, testTimeout)
      ms.length must equal(2)
      ms.exists(_.getClass == classOf[NeedPaper]) must equal(true)
      ms.exists(_.getClass == classOf[NeedCigarette]) must equal(true)
    }
  }
}
