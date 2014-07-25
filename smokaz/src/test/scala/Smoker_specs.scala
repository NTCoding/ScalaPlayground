import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, FreeSpecLike}

class Smoker_specs extends TestKit(ActorSystem.create("smokerSpecs")) with FreeSpecLike with MustMatchers {

  "A smoker will not do anything until it has been invited to the table" in {
    fail
  }

  "If a smoker only has 1 item it needs for smoking" - {

    "When an item it needs is placed on the table" - {

      "It will try and pick it up off the table" in {
        fail
      }
    }
  }

  "If a smoker only has 2 items it needs for smoking" - {

    "When the remaining item is placed on the table" - {

      "It will try and pick it up off the table" in {
        fail
      }
    }
  }

  "If a smoker has all 3 items it needs" - {

    "When any item is placed on the table" - {

      "It will not try and pick it up" in {
        fail
      }
    }
  }

  "When a smoker picks up an item from the table" - {

    "It will celebrate picking up the item" in {
      fail
    }
  }

  "When a smoker is beaten to an item on the table by another smoker" - {

    "It will have an addict fit" in {
      fail
    }
  }

  "When a smoker finishes smoking" - {

    "It will start craving for the items it needs" in {
      fail
    }
  }
}
