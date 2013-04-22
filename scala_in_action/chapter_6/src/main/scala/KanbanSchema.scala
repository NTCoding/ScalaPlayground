import java.sql.DriverManager
import org.squeryl.adapters.H2Adapter
import org.squeryl._
import org.squeryl.PrimitiveTypeMode._

object KanbanSchema extends Schema {

    val stories = table[Story]("STORIES")

    def init = {
        import org.squeryl._

        Class.forName("org.h2.Driver")

        if(SessionFactory.concreteFactory.isEmpty) {
            SessionFactory.concreteFactory = Some(() => Session.create(
                DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", ""), new H2Adapter))
        }
    }

    def main(args: Array[String]) {
        println("initializing the weKanban schema")
        init
        inTransaction { drop ; create }
    }
}
