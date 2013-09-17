package atmo_demo

import org.atmosphere.config.service.{Disconnect, Ready, ManagedService}
import org.atmosphere.cpr._

@ManagedService(path = "/search")
class Searcher {
    private var factory: BroadcasterFactory = null

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
        b.broadcast(s"Received message: $m")
    }
}


