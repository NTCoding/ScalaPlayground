
object ImplicitConversions extends App {

    implicit def imGivingYouASingFunction[A](nonSinger: A) = new {
        def sing() = println(nonSinger.getClass + " is singing now")
    }

    class IDontHaveASingFunction {

    }

    val singer = new IDontHaveASingFunction()
    singer.sing()
}




