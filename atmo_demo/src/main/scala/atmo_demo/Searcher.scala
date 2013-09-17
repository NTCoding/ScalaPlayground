package atmo_demo

import org.atmosphere.config.service.{Disconnect, Ready, ManagedService}
import org.atmosphere.cpr._
import akka.actor.{Props, ActorSystem, Actor}

@ManagedService(path = "/search")
class Searcher {
    private var factory: BroadcasterFactory = null
    private lazy val system: ActorSystem = ActorSystem.create("atmoDemo")

    @Ready
    def onReady(r: AtmosphereResource) {
        factory = r.getAtmosphereConfig.getBroadcasterFactory
    }

    @Disconnect
    def onDisconnect(event: AtmosphereResourceEvent) {
    }

    @org.atmosphere.config.service.Message
    def onMessage(m: String) {
        org.slf4j.LoggerFactory.getLogger(classOf[Searcher]).info(s"Received message: $m")
        val b: Broadcaster = factory.lookup("/search")
        // create a search actor and send it the  message
        (system actorOf Props(classOf[SearchActor], b)) ! m
        
    }
}

class SearchActor(b: Broadcaster) extends Actor {

    def receive = {
        // prepend message with a breaking bad quote and send to originator
        case msg: String => b.broadcast(s"Fat Stax, yo! $msg")
    }
}


