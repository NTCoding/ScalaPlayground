
package object Implicits {

  implicit val birthdays: List[Int] = List(1, 10, 28)

  implicit def stringToMoose(s: String): Moose = new Moose(s)
}
