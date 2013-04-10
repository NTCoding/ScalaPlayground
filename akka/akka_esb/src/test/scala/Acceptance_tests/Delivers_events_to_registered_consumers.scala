package Acceptance_tests

import org.junit._
import junit.framework.Assert._
import Host._
import com.rabbitmq.client._

class Delivers_events_to_registered_consumers {
	
	val eventName = "Score_calculation_updated"
	val consumers = List( ("localhost", "PayoutService"), ("localhost", "CalcualationService") )
	val testMessage = (eventName, (50, 100))
	
	@Before
	def when_an_event_is_published {
								
		val configuration = List ( List(eventName, consumers(0), consumers(1)) )
		
		ensure_queues_exist_and_are_empty
				
		val host = new scalaEsbHost(configuration)
		
		host.start
		
		host.publish(testMessage)
	}
	
	@Test
	def the_event_is_delivered_to_the_queue_of_each_consumer {
		for((host, queue) <- consumers) {
			queryQueue(channel => {
				val message = channel.basicGet(queue, true)
				assertNotNull(message)
				assertEquals(testMessage.toString, new String(message.getBody))
			}, queue)
		}
	}
		
	@After
	def ensure_queues_exist_and_are_empty { for((host, queue) <- consumers) queryQueue(channel => channel.queuePurge(queue), queue) }
	
	def queryQueue(query: Channel => Unit, queue: String) {
		val factory = new ConnectionFactory()
		val c = factory.newConnection()
		val ch = c.createChannel()
		ch.exchangeDeclare(queue, "direct", true)
		ch.queueDeclare(queue, true, false, false, null)
		ch.queueBind(queue, queue, queue)
		query(ch)
		ch.close
		c.close
	}
	
	
}