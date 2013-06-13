package chapter_3

import org.scalatest.FreeSpec

class VarianceBadboys extends FreeSpec {

    "magic can handles any type of water trinket" in {
        val can = new MagicCan[DrinksHolder](x => println(x.iCan))
        can.doIt(new Trinket)
        can.doIt(new WaterCannister)
    }
}


class MagicCan[-A](op: A => Unit) {

    def doIt(item: A) {
        op(item)
    }
}

class WaterCannister extends DrinksHolder {

    def iCan = "pump water into your body faster than a fireman's hosepipe"
}

class Trinket extends DrinksHolder {

    def iCan = "hold a tiny bit of water"
}


trait DrinksHolder {

    def iCan : String
}
