import KanbanSchema._
import org.squeryl._
import org.squeryl.annotations._
import org.squeryl.PrimitiveTypeMode._


class Story(val number: String, val title: String, val phage: String) {

    private[this] def validate = {

        if(number.isEmpty || title.isEmpty)
            throw new ValidationException("Both number and title are required")

        if(!stories.where(s => s.number === number).isEmpty)
            throw new ValidationException("The story number is not unique")
    }

    def tx[A](a: => A): A = {
        init
        inTransaction(a)
    }

    def save(): Either[Throwable, String] = {
        tx {
            try{
                validate
                stories.insert(this)
                Right("Story is created successfully")
            } catch {
                case exception: Throwable => Left(exception)
            }
        }
    }
}

object Story {
    def apply(number: String, title: String) = new Story(number, title, "ready")
}

class ValidationException(message: String) extends RuntimeException(message)
