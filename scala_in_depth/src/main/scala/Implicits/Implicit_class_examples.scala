package Implicits

object Implicit_class_examples {

  def main(args: Array[String]) {
    val x: Int = 5
    // method doesn't exist on the integer class
    5.blowUp()
  }

  implicit class Detonator(n: Int) {
    def blowUp() = println(s"Super Boom x$n")
  }

}
