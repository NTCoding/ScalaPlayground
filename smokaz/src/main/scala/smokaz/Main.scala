package smokaz

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration.FiniteDuration
import scala.util.Random

object Main extends App {

  override def main(args: Array[String]) {
    val config = ConfigFactory.load()
    val system = ActorSystem.create("Smokaz", config)
    val t = new RandomTimeoutGenerator

    system.log.debug("Starting up system")

    val dealer = system.actorOf(Props(classOf[Dealer], t, new RandomItemSelector), "dealer")
    val needsCig = system.actorOf(Props(classOf[NeedACigSmoker]), "needACigSmoker")
    val needsMatch = system.actorOf(Props(classOf[NeedAMatchSmoker]), "needAMatchSmoker")
    val needsPaper = system.actorOf(Props(classOf[NeedPaperSmoker]), "needPaperSmoker")

    needsCig ! "StartSmoking"
    needsMatch ! "StartSmoking"
    needsPaper ! "StartSmoking"

    Thread.sleep(2000)

    dealer ! "StartDealing"
  }

}

trait TimeoutGenerator {
  def generate(): FiniteDuration
}

class RandomTimeoutGenerator {
  import scala.concurrent.duration._
  private val r = new Random
  def generate = r.nextInt(10) seconds
}

trait SmokingItemSelector {
  def select(): SmokingItem
}

class RandomItemSelector extends SmokingItemSelector {
  private val r = new Random

  def select() = r.shuffle(items).head

  private def items = Seq(new Cigarette, new Match, new Paper) // wasteful object allocation - ooooooooooooooh
}

trait SmokingItem {}
class Cigarette extends SmokingItem {}
class Match extends SmokingItem {}
class Paper extends SmokingItem {}
