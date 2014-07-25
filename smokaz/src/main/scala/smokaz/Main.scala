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
    val hasCigs = system.actorOf(Props(classOf[Smoker], Set(new Cigarette)), "hasCigsSmoker")
    val hasMatches = system.actorOf(Props(classOf[Smoker], Set(new Match)), "hasMatchesSmoker")
    val hasPaper = system.actorOf(Props(classOf[Smoker], Set(new Paper)), "hasPaperSmoker")

    hasCigs ! "DealerIsDealing"
    hasMatches ! "DealerIsDealing"
    hasPaper ! "DealerIsDealing"

    Thread.sleep(2000)

    dealer ! "BatchOfSmokingItems" // one day I'll make this a case object
  }

}

trait TimeoutGenerator {
  def generate(): FiniteDuration
}

class RandomTimeoutGenerator extends TimeoutGenerator {
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
case class Cigarette() extends SmokingItem {}
case class Match() extends SmokingItem {}
case class Paper() extends SmokingItem {}
