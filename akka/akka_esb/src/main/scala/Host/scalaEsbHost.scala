package Host

import com.rabbitmq.client._
import akka.routing.RoundRobinRouter
import akka.actor._

case class Publish(eventName: String, data: (Any))
case class Stop
case class DeliverEvent(name: String, message: (Any), service: String)


class scalaEsbHost(val configuration: List[List[Any]]) {
	
	var master: ActorRef = _
	
	def start {
		val sys = ActorSystem("akkaEsbSystem")
		master = sys.actorOf(Props(new Master(configuration)))
	}
	
	def publish(event: Any) {
		event match {
			case (eventName: String, data: (Any)) => master ! Publish(eventName, data)
			case _ => println("Not a valid event")
		}
	}
		
	def stop { master ! Stop }
}

class Master(configuration: List[List[Any]]) extends Actor {
	
	val workerRouter = context.actorOf(Props(new PublishCoordinator(configuration)).withRouter(RoundRobinRouter(50)))
	
	def receive = {
		case Publish(event, message) => workerRouter ! Publish(event, message)
		case Stop => context.system.shutdown
	}
}

class PublishCoordinator(configuration: List[List[Any]]) extends Actor {
	
	val workerRouter = context.actorOf(Props[PublishWorker].withRouter(RoundRobinRouter(50)))
	
	def receive = {
		case Publish(event, message) => distributePublishingAcrossWorkers(event, message) 
	}
	
	def distributePublishingAcrossWorkers(event: String, message: (Any)) {
		val consumersOfEvent = configuration.filter(l => l.head.toString == event)(0).tail
		for((host, service) <- consumersOfEvent){
			println(f"Delegating to worker: $event $message $service")
			workerRouter ! DeliverEvent(event, message, service.toString)
		}
	}
}

class PublishWorker extends Actor {
	
	def receive = {
		case DeliverEvent(event, message, service) => sendMessageToQueue(service, (event, message))
	}
	
	def sendMessageToQueue(service: String, message: (String,(Any))) {
		val factory = new ConnectionFactory()
		val c = factory.newConnection()
		val ch = c.createChannel()
		ch.exchangeDeclare(service, "direct", true)
		ch.queueDeclare(service, true, false, false, null)
		ch.queueBind(service, service, service)
		println(f"publishing to $service: " + message.toString)
		ch.basicPublish(service, service, null, message.toString.getBytes)
		ch.close
		c.close
	}
}