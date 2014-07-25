import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

object Main extends App {

  override def main(args: Array[String]) {
    val config = ConfigFactory.load()
    val system = ActorSystem.create("Smokaz", config)

    system.log.debug("Starting up system")

    val dealer = system.actorOf(Props(classOf[Dealer]), "dealer")
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
