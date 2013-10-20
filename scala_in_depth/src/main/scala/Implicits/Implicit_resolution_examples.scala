package Implicits

object ImportedScope {
  implicit val names: Array[String] = Array("Jamie", "Jonas", "Viktor")
}

object Builder {
  implicit val tezza: CockneyBuilder = new CockneyBuilder("Fat Tezza")
}

abstract class Builder {
  val name: String
}

case class CockneyBuilder(name: String) extends Builder

object Giraffe {
  implicit val gerry: Giraffe = Giraffe("Gerry")
}

case class Giraffe(name: String)

object Truck {
  implicit val topTrucks = List(Truck("big truck"), Truck("fast truck"))
}

case class Truck(name: String)

class ImplicitResolution {
  def implicitsCanComeFromLocalScope(implicit s: String) = println(s)
  def implicitsCanComeFromImportedScope(implicit i: Array[String]) = println(i mkString ",")
  def implicitsCanComeFromTheCompanionObject(implicit g: Giraffe) = println(g.name)
  def implicitsCanComeFromBaseClassCompanion(implicit fi: CockneyBuilder) = println(fi.name)
  def implicitsCanComeFromGenericTypeCompanion(implicit x: List[Truck]) = {
    println(x map(_.name) mkString ",")
  }
  def implicitsCanComeFromPackageObjects(implicit y: List[Int]) = println(y mkString ",")
}

object Main {
  def main(args: Array[String]) {
    val imp = new ImplicitResolution()

    implicit val st: String = "implicit val in scope"
    imp.implicitsCanComeFromLocalScope

    import ImportedScope._
    // array of strings come from imported scope in line above
    imp.implicitsCanComeFromImportedScope

    // giraffe instance comes from companion object of giraffe
    imp.implicitsCanComeFromTheCompanionObject

    // implicit cockney idiot defined in base class idiot's companion object
    imp.implicitsCanComeFromBaseClassCompanion

    // finds list of truck in truck companion because truck is generic type?
    imp.implicitsCanComeFromGenericTypeCompanion

    // implicit list of integers declared in package object
    imp.implicitsCanComeFromPackageObjects
  }
}
