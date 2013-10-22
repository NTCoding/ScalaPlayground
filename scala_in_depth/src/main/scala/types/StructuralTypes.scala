package types

object StructuralTypes {

  type Riddler = {
    def tellJoke()
  }

  def riddlerTrick(r: Riddler) = r.tellJoke()

  val rid = new Ridzoid
  riddlerTrick(rid)
}

class Ridzoid {
  def tellJoke() = println("What's black and white and red all over? Your Mom")
}
