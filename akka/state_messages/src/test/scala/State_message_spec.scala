import akka.actor.{ActorSystem, Actor}
import akka.testkit.{TestKit, TestActorRef}
import org.scalatest.{FreeSpecLike, FreeSpec, MustMatchers}
import scala.concurrent.duration.Duration

class State_message_spec extends TestKit(ActorSystem.create("test")) with FreeSpecLike with MustMatchers {

    "Librarian will return state regardless of what is sent to her" in {
        val librarian = TestActorRef[Librarian]

        val firstState = List[String]("this", "is", "some", "state")
        librarian ! BookRequest("Akka in Action", firstState)
        receiveOne(Duration(100, "ms")) match {
            case HereIsTheBook(name, state) => state must equal(firstState)
        }

        val secondState = ("Tuple", ("st", "ate"))
        librarian ! BookRequest("Scala in Action", secondState)
        receiveOne(Duration(100, "ms")) match {
            case HereIsTheBook(name, state) => state must equal(secondState)
        }

    }

}

class Librarian extends Actor {

    val books = List[Book](
       Book("Akka in Action", Array(Byte.MinValue)),
       Book("Scala in Action", Array(Byte.MinValue)),
       Book("Functional programming in Scala", Array(Byte.MinValue))
    )

    def findBook(name: String) = books.filter(_.name == name).headOption

    def receive = {
        case BookRequest(name, state) => {
            findBook(name) match {
                case None => sender ! BookNotFound(name, state)
                case Some(book: Book) => sender ! HereIsTheBook(book, state)
            }
        }
    }

}

case class BookRequest(name: String, state: AnyRef)

case class Book(name: String, binary: Array[Byte])

case class BookNotFound(name: String, state: AnyRef)

case class HereIsTheBook(book: Book, state: AnyRef)

// option 1 - always set last as argument as state

// option 2 - all messages are tuples, and second value is always state

// option 3 - create a class tha encapsulates this rule
