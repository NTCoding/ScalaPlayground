package Implicits

class ImplicitConversions {
  def implicitConversionCanComeFromLocalScope(implicit g: Gorilla) = println(g.name)
  def implicitConversionsCanComeFromImportedScope(implicit p: Panda) = println(p.name)
  def implicitConversionsCanComeFromCompanion(implicit e: Elephant) = println(e.name)
  def implicitConversionsCanComeFromBaseCompanion(implicit j: Joker) = println(j.name)
  def implicitConversionsCanComeFromGenericType(implicit t: List[Turtle]) = println(t map (_.name) mkString ",")
  def implicitConversionsCanComeFromPackageObject(implicit m: Moose) = println(m.name)
}

case class Gorilla(name: String)
case class Panda(name: String)
case class Elephant(name: String)
abstract class FunnyMan() { val name: String }
case class Joker(name: String) extends FunnyMan
case class Turtle(name: String)
case class Moose(name: String)

object ImportedConversionsScope {
  implicit def stringToPanda(s: String) = new Panda(s)
}

object Elephant {
  implicit def stringToElephant(s: String): Elephant = new Elephant(s)
}

object FunnyMan {
  implicit def stringToJoker(s: String): Joker = new Joker(s)
}

object Turtle {
  implicit def stringToTurtle(s: String): Turtle = new Turtle(s)
}

object Implicit_conversion_examples {
  def main(args: Array[String]) {
    val imp = new ImplicitConversions

    implicit def stringToGorilla(s: String): Gorilla = new Gorilla(s)
    imp.implicitConversionCanComeFromLocalScope("Big Pappa Gorillz")

    import ImportedConversionsScope._
    imp.implicitConversionsCanComeFromImportedScope("Small Sally Panda")

    imp.implicitConversionsCanComeFromCompanion("Eze ears")

    imp.implicitConversionsCanComeFromBaseCompanion("Jokeezey")

    // string to turtle conversion applied to each item in list of strings
    imp.implicitConversionsCanComeFromGenericType(List("little tuts", "big tuts"))

    imp.implicitConversionsCanComeFromPackageObject("I wanna be a moose")
  }
}
