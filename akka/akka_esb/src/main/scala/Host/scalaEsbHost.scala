package Host

import com.rabbitmq.client._

class scalaEsbHost(val configuration: List[List[Any]]) {
	
	def publish(event: Any) {
		event match {
			case (eventName: String, data: (Any)) => publishToConsumers(eventName, data)
			case _ => println("Not a valid event")
		}
	}
	
	def publishToConsumers(eventName: String, data: (Any)) {
		val consumersOfEvent = configuration.filter(l => l.head.toString == eventName)(0).tail
		for((host, service) <- consumersOfEvent){
			sendMessageToQueue(service.toString, (eventName, data))
		}
	}
	
	def sendMessageToQueue(service: String, message: (String,(Any))) {
		val factory = new ConnectionFactory()
		val c = factory.newConnection()
		val ch = c.createChannel()
		ch.exchangeDeclare(service, "direct", true)
		ch.queueDeclare(service, true, false, false, null)
		ch.queueBind(service, service, service)
		ch.basicPublish(service, service, null, message.toString.getBytes)
		ch.close
		c.close
	}
	
	def start {	}
}