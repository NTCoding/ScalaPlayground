import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, FreeSpecLike}

class Table_specs extends TestKit(ActorSystem.create("tableSpecs")) with FreeSpecLike with MustMatchers {

  "If an item is put on the table" - {

    "A smoker can pick it up" in {
      fail
    }
  }

  "If a smoker tries to pick up an item that is no longer on the table" - {

    "The smoker will be told it's already gone" in {
      fail
    }
  }
}
